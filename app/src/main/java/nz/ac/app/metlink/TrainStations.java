package nz.ac.app.metlink;

/**
 * Created by 21600481 on 15-10-2016.
 */
public class TrainStations{
        private String  trainStationName;
    public TrainStations(){}
        public TrainStations(String trainStationName)
        {

            this.trainStationName=trainStationName;
        }


        public String gettrainStationName()

        {
            return this.trainStationName;
        }
        public void settrainStationName(String trainStationName)
        {
            this.trainStationName=trainStationName;
        }
}
