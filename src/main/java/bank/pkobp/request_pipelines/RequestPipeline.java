package bank.pkobp.request_pipelines;

import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.request_processors.AbstractRequestProcessor;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestPipeline {

    private final List<PipelineStep<?, ?>> steps;

    public RequestPipeline() {
        this.steps = new ArrayList<>();
    }

    public <R, S> void addStep(AbstractRequestProcessor<R, S> processor, R request, TypeReference<S> responseType) {
        steps.add(new PipelineStep<>(processor, request, responseType));
    }

    public void executePipeline() throws IOException, RequestProcessingException {
        for (PipelineStep<?, ?> step : steps) {
            step.execute();
        }
    }

    private static class PipelineStep<R, S> {
        private final AbstractRequestProcessor<R, S> processor;
        private final R request;
        private final TypeReference<S> responseType;

        public PipelineStep(AbstractRequestProcessor<R, S> processor, R request, TypeReference<S> responseType) {
            this.processor = processor;
            this.request = request;
            this.responseType = responseType;
        }

        public void execute() throws IOException, RequestProcessingException {
            processor.executeRequest(request, responseType);
        }
    }
}