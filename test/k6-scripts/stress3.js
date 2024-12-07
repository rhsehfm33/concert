import http from 'k6/http'
import {sleep} from 'k6';

const userAmount = 600;
const port = 8080;

export let options = {
    stages: [
        { duration: '1s', target: userAmount },
    ],
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

export function setup() {
    http.del(`http://host.docker.internal:${port}/dummy/queue-tokens`);
    http.put(`http://host.docker.internal:${port}/dummy/concerts/${userAmount}`);

    const vuData = {};
    for (let userId = 1; userId <= userAmount; userId++) {
        const res = http.post(`http://host.docker.internal:${port}/dummy/queue-tokens/${userId}`);
        try {
            vuData[userId] = JSON.parse(res.body)['queueResult']['uuid'];
        } catch (e) {
            throw new Error(`JSON.parse failed for VU ${userId}: ${e}`);
        }
    }
    return vuData;
}

export default function(vuData) {
    const uuid = vuData[__VU];

    sleep(randomInteger(1, 20));
    const reservationId =  reserveSeat(uuid, __VU);
    sleep(randomInteger(1, 20));
    chargePoint(uuid, 20000);
    sleep(randomInteger(1, 20));
    paySeat(uuid, reservationId);
}

function reserveSeat(uuid, seatId) {
    const payload = JSON.stringify({
        seatId: seatId
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${uuid}`
        }
    };
    const url = `http://host.docker.internal:${port}/v1/protected/seat-reservations`;
    const res = http.post(url, payload, params);
    return JSON.parse(res.body)['seatReservationResult']['seatReservation']['id'];
}

function chargePoint(uuid, amount) {
    const payload = JSON.stringify({
        amount: amount
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${uuid}`
        }
    };
    const url = `http://host.docker.internal:${port}/v1/protected/users/point`;
    http.post(url, payload, params);
}

function paySeat(uuid, reservationId) {
    const payload = JSON.stringify({
        reservationId: reservationId
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${uuid}`
        }
    };
    const url = `http://host.docker.internal:${port}/v1/protected/seat-reservations/payment`;
    http.post(url, payload, params);
}

// min <= x <= max
function randomInteger(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
