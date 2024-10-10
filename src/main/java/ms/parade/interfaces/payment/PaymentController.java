package ms.parade.interfaces.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ms.parade.interfaces.common.MessageResponse;

@RestController
@RequestMapping("/v1")
public class PaymentController {

    @PostMapping("/seat-reservations/payment")
    ResponseEntity<MessageResponse> paySeatReservation(
        @RequestBody SeatPaymentRequest seatPaymentRequest
    ) {
        return ResponseEntity.ok(new MessageResponse("성공적으로 예약한 좌석을 결제했습니다."));
    }
}
