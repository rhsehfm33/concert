import http from 'k6/http'
import {sleep} from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 1000 },
        { duration: '30s', target: 1000 },
    ],
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
};


export default function() {
    const userId = __VU;
    let req = http.post(`http://host.docker.internal:8081/v1/queue-tokens/${userId}`);
    try {
        const uuid = JSON.parse(req.body)['queueResult']['uuid'];

        http.get(`http://host.docker.internal:8081/v1/queue-tokens/${uuid}`);
        sleep(10);
    } catch (e) {
        console.error(e);
    }
}