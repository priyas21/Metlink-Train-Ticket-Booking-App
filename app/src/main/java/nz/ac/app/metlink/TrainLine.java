package nz.ac.app.metlink;

/**
 * Created by 21600481 on 15-10-2016.
 */
public class TrainLine {

    private String  trainLineName;
    public TrainLine(String trainLineName)
    {

        this.setTrainLineName(trainLineName);
    }


    public String getTrainLineName()
    {
        return trainLineName;
    }
    public void setTrainLineName(String trainLineName)
    {
        this.trainLineName=trainLineName;
    }


}
