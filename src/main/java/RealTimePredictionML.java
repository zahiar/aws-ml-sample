import com.amazonaws.services.machinelearning.AmazonMachineLearning;
import com.amazonaws.services.machinelearning.AmazonMachineLearningClientBuilder;
import com.amazonaws.services.machinelearning.model.*;

public class RealTimePredictionML {

    public static void main(String[] args) {
        System.out.println("Create ML Client");

        AmazonMachineLearning awsMl = AmazonMachineLearningClientBuilder.standard()
            .withRegion("eu-west-1")
            .build();


        System.out.println("Get list of Models");
        DescribeMLModelsResult mlModels = awsMl.describeMLModels();
        for (MLModel m : mlModels.getResults()) {
            System.out.println("Model name: " + m.getName());
            System.out.println("Model id: " + m.getMLModelId());
            System.out.println("Model status: " + m.getStatus());
        }

        System.out.println("Select Model - we'll pick the first one for this sample");
        MLModel mlModel = mlModels.getResults().get(0);

        System.out.println("Create Real-Time Endpoint -- note this takes time to create");
        CreateRealtimeEndpointRequest realtimeEndpointRequest = new CreateRealtimeEndpointRequest();
        realtimeEndpointRequest.setMLModelId(mlModel.getMLModelId());

        CreateRealtimeEndpointResult realtimeEndpointResult = awsMl.createRealtimeEndpoint(realtimeEndpointRequest);
        System.out.println(realtimeEndpointResult.toString());

        System.out.println("Predictions\n\n");

        // Format: {username, depositAmount, date, what we expect the result to be (so we can assert what the ML predicts)}
        // Model used was to predict unusual deposit amounts
        String[][] predictions = new String[][]{
                {"a", "5", "01/03/2017", "0"}, // Usual deposit amount
                {"a", "500", "01/03/2017", "0"}, // Way above their average but first time doing it so not a problem until they do it again
                {"b", "10", "01/02/2017", "1"}, // Double their average and second time they've deposited this (see CSV)
        };

        PredictRequestCreator predictRequestCreator = new PredictRequestCreator(mlModel, realtimeEndpointResult.getRealtimeEndpointInfo());
        for (String[] prediction: predictions) {
            PredictResult predictResult = awsMl.predict(predictRequestCreator.buildRequest(prediction[0], prediction[1], prediction[2]));

            if (predictResult.getPrediction().getPredictedLabel().compareTo("1") == 0) {
                System.out.print("Unexpected deposit amount");
            } else {
                System.out.print("Usual deposit amount");
            }

            // This is just to check if the prediction from the ML Model matches what we expected
            if (predictResult.getPrediction().getPredictedLabel().compareTo(prediction[3]) == 0) {
                System.out.println(" ---- EXPECTED RESULT");
            } else {
                System.out.println(" ---- NOT EXPECTED RESULT");
            }
        }

        System.out.println("Delete Real-Time Endpoint - keeping it alive will cost money!");
        DeleteRealtimeEndpointRequest deleteRealtimeEndpointRequest = new DeleteRealtimeEndpointRequest();
        deleteRealtimeEndpointRequest.setMLModelId(mlModel.getMLModelId());

        DeleteRealtimeEndpointResult deleteRealtimeEndpointResult = awsMl.deleteRealtimeEndpoint(deleteRealtimeEndpointRequest);
        System.out.println(deleteRealtimeEndpointResult.toString());
    }

}