package hotel;

public class Payment {
    private double amount;
    private boolean isPaymentPending;

    public Payment(double amount) {
        this.amount = amount;
        this.isPaymentPending = true;  
    }

  
    public boolean processPayment() {
        
        return isPaymentPending;
    }

    public void markPaymentAsPending() {
        isPaymentPending = true;  
    }

    public void markPaymentAsCompleted() {
        isPaymentPending = false;  
    }

    public boolean isPaymentPending() {
        return isPaymentPending;
    }

    public double getAmount() {
        return amount;
    }
}
