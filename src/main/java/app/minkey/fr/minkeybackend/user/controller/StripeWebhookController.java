package app.minkey.fr.minkeybackend.user.controller;

import app.minkey.fr.minkeybackend.user.model.Plan;
import app.minkey.fr.minkeybackend.user.service.UserService;
import com.stripe.Stripe;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.param.checkout.SessionListLineItemsParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@RestController
@RequestMapping("/api/stripe/webhook")
public class StripeWebhookController {

    private final UserService userService;
    private Plan choicePlan = null;

    public StripeWebhookController(UserService userService) {
        this.userService = userService;
    }

    static {
        Stripe.apiKey =
                "sk_test_51QVWQ8IcY2LL5iPD7ROuiG6nBwAGVRKWydIpyruuCLEq22yEE1PbwWK3ta9be5Y5eZwdVnXwbCRh8iKpgC5HxACa00q3o8JSXC";
    }

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload) {
        Event event;
        try {
            // Désérialisation de l'événement à partir du payload JSON
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (Exception e) {
            System.err.println("Invalid payload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }

        StripeObject stripeObject = null;

        // Désérialiser l'objet à partir des données de l'événement
        if (event.getDataObjectDeserializer().getObject().isPresent()) {
            stripeObject = event.getDataObjectDeserializer().getObject().get();
        } else {
            System.err.println("Deserialization of StripeObject failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to deserialize Stripe object");
        }

        // Gérer les différents types d'événements
        switch (event.getType()) {
            case "checkout.session.completed":
                if (stripeObject instanceof Session) {
                    Session session = (Session) stripeObject;
                    handleCheckoutSessionCompleted(session);
                }
                break;
            case "payment_intent.succeeded":
                if (stripeObject instanceof PaymentIntent) {
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    handlePaymentIntentSucceeded(paymentIntent);
                }
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok("Webhook processed successfully");
    }

    private void handleCheckoutSessionCompleted(Session session) {
        System.out.println("Checkout Session Completed: " + session.getId());
        System.out.println("Checkout Session Completed: " + session.getCustomer());
        if (Stripe.apiKey == null || Stripe.apiKey.isEmpty()) {
            throw new RuntimeException("Stripe API Key is not configured.");
        }
        try {
            SessionListLineItemsParams params = SessionListLineItemsParams.builder()
                    .setLimit(5L)
                    .build();

            LineItemCollection lineItems = session.listLineItems(params);

            if (!lineItems.getData().isEmpty()) {
                String productName = lineItems.getData().get(0).getDescription(); // Utilisation de la première ligne
                System.out.println("Product Name: " + productName);

                switch (productName) {
                    case "Ultimate month":
                        choicePlan = Plan.ULTIMATE;
                        break;
                    case "premium mk months":
                        choicePlan = Plan.PREMIUM;
                        break;
                    default:
                        System.out.println("Unhandled product: " + productName);
                        break;
                }

                userService.updateSub(
                        session.getCustomer().toString(),
                        choicePlan,
                        new Timestamp(System.currentTimeMillis()));
            } else {
                System.out.println("No line items found for this session.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving line items: " + e.getMessage());
        }
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        System.out.println("Payment Intent Succeeded: " + paymentIntent.getId());
        // Logique pour traiter le paiement réussi
    }
}