package ch.epfl.sweng.radius.database;

public class OthersInfo extends DBObservable{

    private static OthersInfo othersInfo = null;
    private static final Database database = Database.getInstance();


    public static OthersInfo getInstance(){
        if (othersInfo == null)
            othersInfo = new OthersInfo();
        return othersInfo;
    }

    private OthersInfo(){
    }
}
