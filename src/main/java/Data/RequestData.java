package Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class RequestData {

    private long UnixTime;
    private String UserName;
    private String PCName;
    private String PersonellNumber;
    private int RobotID;
    private int AccID;

    public RequestData(){ }

    public void setUnixTime (String unixTime){
        try {
            this.UnixTime = Long.parseLong(unixTime);
        }catch (Exception e){
            this.UnixTime = 1;
        }
    }

    public void setUserName (String UserName){
        this.UserName = UserName;
    }

    public void setPCName (String PCName){
        this.PCName = PCName;
    }

    public void setPersonellNumber (String PersonellNumber){
        this.PersonellNumber = PersonellNumber;
    }

    public void setRobotID (String RobotID){
        try{
            this.RobotID = Integer.parseInt(RobotID);
        }catch (Exception e){
            this.RobotID = 0;
        }
    }

    public void setAccID (String AccID){
        try {
            this.AccID = Integer.parseInt(AccID);
        }catch (Exception e){
            this.AccID = 0;
        }
    }

    public int getAccID() {
        return AccID;
    }

    public int getRobotID() {
        return RobotID;
    }

    public boolean UnixTimeCheck(long delay){
        //День взятия Жепы
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+00:00")) -
                LocalDateTime.of(1995,7,25,0,0,0).toEpochSecond(ZoneOffset.of("+00:00"));

        if ((now - this.UnixTime) > delay){
            return false;
        }
        return true;
    }
}
