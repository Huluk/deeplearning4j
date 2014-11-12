package org.deeplearning4j.nn.layers;



import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import org.deeplearning4j.nn.WeightInit;
import org.deeplearning4j.nn.WeightInitUtil;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * A layer with a bias and activation function
 * @author Adam Gibson
 */
public abstract class BaseLayer implements Layer {

    protected INDArray W;
    protected INDArray b;
    protected INDArray input;
    protected NeuralNetConfiguration conf;
    protected INDArray dropoutMask;



    public BaseLayer(NeuralNetConfiguration conf,INDArray W, INDArray b, INDArray input) {
        this.input = input;
        this.conf = conf;

        if(W == null)
            this.W = createWeightMatrix();



        else
            this.W = W;


        if(b == null)
            this.b = createBias();
        else
            this.b = b;
    }


    protected INDArray createBias() {
        return Nd4j.zeros(conf.getnOut());
    }


    protected INDArray createWeightMatrix() {
        INDArray W = WeightInitUtil.initWeights(
                conf.getnIn(),
                conf.getnOut(),
                conf.getWeightInit(),
                conf.getActivationFunction(),
                conf.getDist());
        return W;
    }


    /**
     * Classify input
     * @param x the input (can either be a matrix or vector)
     * If it's a matrix, each row is considered an example
     * and associated rows are classified accordingly.
     * Each row will be the likelihood of a label given that example
     * @return a probability distribution for each row
     */
    @Override
    public  INDArray preOutput(INDArray x) {
        if(x == null)
            throw new IllegalArgumentException("No null input allowed");

        this.input = x;

        INDArray ret = this.input.mmul(W);
        if(ret.columns() != b.columns())
            throw new IllegalStateException("This is weird");
        if(conf.isConcatBiases())
            ret = Nd4j.hstack(ret,b);
        else
            ret.addiRowVector(b);
        return ret;


    }


    @Override
    public  INDArray activate() {
        INDArray activation =  conf.getActivationFunction().apply(getInput().mmul(getW()).addRowVector(getB()));
        return activation;
    }

    @Override
    public  INDArray activate(INDArray input) {
        if(input != null)
            this.input = Transforms.stabilize(input, 1);
        return activate();
    }




    @Override
    public NeuralNetConfiguration conf() {
        return conf;
    }

    @Override
    public void setConfiguration(NeuralNetConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public INDArray getW() {
        return W;
    }

    @Override
    public void setW(INDArray W) {
        assert W.rows() == conf().getnIn() && W.columns() == conf.getnOut() : "Weight matrix must be of shape " + Arrays.toString(new int[]{conf().getnIn(),conf.getnOut()});
        this.W = W;
    }

    @Override
    public INDArray getB() {
        return b;
    }

    @Override
    public void setB(INDArray b) {
        assert b.columns() == conf().getnOut() : "The bias must have " + conf().getnOut() + " columns";
        this.b = b;
    }

    @Override
    public INDArray getInput() {
        return input;
    }

    @Override
    public void setInput(INDArray input) {
        this.input = input;
    }




    protected void applyDropOutIfNecessary(INDArray input) {
        if(conf.getDropOut() > 0) {
            INDArray mask = Nd4j.rand(input.rows(), input.columns());
            mask.gti(2);
            this.dropoutMask = Nd4j.rand(input.rows(), input.columns()).gt(conf.getDropOut());
        }

        else
            this.dropoutMask = Nd4j.ones(input.rows(), conf.getnOut());

        //actually apply drop out
        input.muli(dropoutMask);

    }

    /**
     * Averages the given logistic regression
     * from a mini batch in to this one
     * @param l the logistic regression to average in to this one
     * @param batchSize  the batch size
     */
    public void merge(Layer l,int batchSize) {
        if(conf.isUseRegularization()) {

            W.addi(l.getW().subi(W).div(batchSize));
            b.addi(l.getB().subi(b).div(batchSize));
        }

        else {
            W.addi(l.getW().subi(W));
            b.addi(l.getB().subi(b));
        }

    }


    @Override
    public Layer clone() {
        Layer layer = null;
        try {
            Constructor c = getClass().getConstructor(NeuralNetConfiguration.class,INDArray.class,INDArray.class,INDArray.class);
            layer = (Layer) c.newInstance(conf,W.dup(),b.dup(),input != null  ? input.dup() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return layer;

    }

    @Override
    public Layer transpose() {
        Layer layer = null;
        try {
            Constructor c = getClass().getConstructor(NeuralNetConfiguration.class,INDArray.class,INDArray.class,INDArray.class);
            NeuralNetConfiguration clone = conf.clone();
            int nIn = clone.getnOut(),nOut = clone.getnIn();
            clone.setnIn(nIn);
            clone.setnOut(nOut);
            layer = (Layer) c.newInstance(conf,W.transpose().dup(),b.transpose().dup(),input != null  ? input.transpose().dup() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return layer;
    }

    @Override
    public String toString() {
        return "BaseLayer{" +
                "W=" + W +
                ", b=" + b +
                ", input=" + input +
                ", conf=" + conf +
                ", dropoutMask=" + dropoutMask +
                '}';
    }
}
