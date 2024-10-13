package ms.parade.interfaces.payment;

public record SeatPaymentRequest(String uuid, long userId, long seatId, String date) {
}
