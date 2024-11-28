import http from 'k6/http';
import {sleep} from 'k6';

const userAmount = 5000;
const port = 8080;

export let options = {
    stages: [
        { duration: '3s', target: userAmount },
        { duration: '1m', target: userAmount }
    ],
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

export function setup() {
    http.del(`http://host.docker.internal:${port}/dummy/queue-tokens`);
    http.put(`http://host.docker.internal:${port}/dummy/concerts/${userAmount}`);
}

// 각 VU마다 고유의 uuid를 저장할 수 있는 객체 설정
let vuUuids = {};

export default function () {
    sleep(randomInteger(1, 20));
    const userId = __VU;  // __VU는 각 VU(가상 사용자)의 고유 ID

    // uuid가 이미 존재하면 POST 요청을 실행하지 않음
    let uuid = vuUuids[userId]
    if (!uuid) {
        let res = http.post(`http://host.docker.internal:${port}/v1/queue-tokens/${userId}`);
        try {
            let responseBody = JSON.parse(res.body);
            vuUuids[userId] = responseBody['queueResult']['uuid']; // uuid 저장
            uuid = vuUuids[userId];
        } catch (e) {
            console.error(`Failed to parse response for VU ${userId}:`, e);
            throw e;
        }
        sleep(randomInteger(1, 20));
    }

    http.get(`http://host.docker.internal:${port}/v1/queue-tokens/${uuid}`);

}

function randomInteger(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
