import com.amazonaws.services.machinelearning.model.MLModel;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import com.amazonaws.services.machinelearning.model.RealtimeEndpointInfo;

class PredictRequestCreator {

    private MLModel mlModel;
    private RealtimeEndpointInfo realtimeEndpointInfo;

    public PredictRequestCreator(MLModel mlModel, RealtimeEndpointInfo realtimeEndpointInfo) {
        this.mlModel = mlModel;
        this.realtimeEndpointInfo = realtimeEndpointInfo;
    }

    public PredictRequest buildRequest(String username, String depositAmount, String date) {
        PredictRequest predictRequest = new PredictRequest();
        predictRequest.setMLModelId(mlModel.getMLModelId());
        predictRequest.setPredictEndpoint(realtimeEndpointInfo.getEndpointUrl());

        predictRequest.addRecordEntry("username", username);
        predictRequest.addRecordEntry("deposit", depositAmount);
        predictRequest.addRecordEntry("date", date);

        return predictRequest;
    }

}