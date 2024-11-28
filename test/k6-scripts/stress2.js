import http from 'k6/http'
import {sleep} from 'k6';

const userAmount = 600;
const port = 8080;

export let options = {
    stages: [
        { duration: '1s', target: userAmount },
        { duration: '1m', target: userAmount },
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

export default function (vuData) {
    viewConcertSchedules(vuData[__VU], 1);
    sleep(randomInteger(1, 20));
    viewSeatsOfConcertSchedule(vuData[__VU], 1);
    sleep(randomInteger(1, 20));
}

function viewConcertSchedules(uuid, concertId) {
    const headers = {
        'Authorization': `${uuid}`
    };
    http.get(
        `http://host.docker.internal:${port}/v1/protected/concerts/${concertId}/available-dates`,
        { headers: headers }
    );
}

function viewSeatsOfConcertSchedule(uuid, scheduleId) {
    const headers = {
        'Authorization': `${uuid}`
    };
    http.get(
        `http://host.docker.internal:${port}/v1/protected/concerts-schedules/${scheduleId}/available-seats`,
        { headers: headers }
    );
}

// min <= x <= max
function randomInteger(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}