package deutschebank.thebeans;

import deutschebank.MainUnit;
import deutschebank.dbutils.DBConnector;
import deutschebank.dbutils.PropertyLoader;
import deutschebank.dbutils.User;
import deutschebank.dbutils.UserHandler;

// NEW STUFF
import deutschebank.dbutils.Instrument;
import deutschebank.dbutils.InstrumentHandler;
import deutschebank.dbutils.InstrumentIterator;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;




// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
import deutschebank.MainUnit;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ApplicationScopeHelper
{
  private String itsInfo = "NOT SET";
  private DBConnector itsConnector = null;
  
  public String getInfo()
  {
    return this.itsInfo;
  }
  
  public void setInfo(String itsInfo)
  {
    this.itsInfo = itsInfo;
  }
  
  public boolean bootstrapDBConnection()
  {
    boolean result = false;
    try
    {
      this.itsConnector = DBConnector.getConnector();
      
      PropertyLoader pLoader = PropertyLoader.getLoader();
      
      Properties pp = pLoader.getPropValues("dbConnector.properties");
      
      result = this.itsConnector.connect(pp);
    }
    catch (IOException ex)
    {
      Logger.getLogger(ApplicationScopeHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
  }
  
  public String userLogin(String userId, String userPwd)
  {
    User theUser = null;
    UserHandler theUserHandler = UserHandler.getLoader();
    
    theUser = theUserHandler.loadFromDB(this.itsConnector.getConnection(), userId, userPwd);
    if (theUser != null) {
      MainUnit.log("User " + userId + " has logged into system");
      return "[{\"loginResult\":\"success\"}]";
    }
    else{
      return "[{\"loginResult\":\"failure\"}]";
    }
  }

  // public ArrayList<Instrument> getAllInstruments(){
  //   ArrayList<Instrument> theInstruments = null;
  //   InstrumentHandler theInstrumentHandler = InstrumentHandler.getLoader();

  //   theInstruments = theInstrumentHandler.loadFromDB("db_grad_cs_1917",this.itsConnector.getConnection());

  //   return theInstruments; 
  // }
  





  public Instrument getInstrumentForKey(int key){
    Instrument theInstrument = null;
    InstrumentHandler theInstrumentHandler = InstrumentHandler.getLoader();

    theInstrument = theInstrumentHandler.loadFromDB("db_grad_cs_1917",this.itsConnector.getConnection(),key);
    if (theInstrument != null) {
      MainUnit.log("Instrument " + key + " extracted");
    }
    return theInstrument;
  }

  public String getInstruments(){
    ArrayList<Instrument> theInstruments = null;
    InstrumentHandler theInstrumentHandler = InstrumentHandler.getLoader();

    theInstruments = theInstrumentHandler.loadFromDB("db_grad_cs_1917",this.itsConnector.getConnection());

    String instrumentResponse = theInstrumentHandler.toJSON(theInstruments);

    return instrumentResponse; 
  }


// stuff for counterparty

  /*
      -- counterpartyID
      -- counterpartyName
      -- counterpartyStatus
      -- counterpartyDateRegistered

  */




  public class Counterparty{
      private int counterpartyID;
      private String counterpartyName;
      private String counterpartyStatus;
      private String counterpartyDateRegistered;


      // CONSTRUCTOR

      public Counterparty(int id, String name, String status, String dateRegistered){
        this.counterpartyID = id;
        this.counterpartyName = name;
        this.counterpartyStatus = status;
        this.counterpartyDateRegistered = dateRegistered;
      }


      // GETTERS

      public int getCounterpartyID(){
        return this.counterpartyID;
      }

      public String getCounterpartyName(){
        return this.counterpartyName;
      }

      public String getCounterpartyStatus(){
        return this.counterpartyStatus;
      }

      public String getCounterpartyDateRegistered(){
        return this.counterpartyDateRegistered;
      }


      // SETTERS

      public void setCounterpartyID(int counterpartyID){
        this.counterpartyID = counterpartyID;
      }

      public void setCounterpartyName(String counterpartyName){
        this.counterpartyName = counterpartyName;
      }

      public void setCounterpartyStatus(String counterpartyStatus){
        this.counterpartyStatus = counterpartyStatus;
      }

      public void setCounterpartyDateRegistered(String counterpartyDateRegistered){
        this.counterpartyDateRegistered = counterpartyDateRegistered;
      }

  }


  // stuff for deal

  // public class Deal{
  //   private int dealID;
  //   private String dealTime;
  //   private int dealCounterpartyID;
  //   private int dealInstrumentID;
  //   private String dealType;
  //   private float dealAmount;
  //   private int dealQuantity;

  //   // CONSTRUCTOR

  //   public Deal(int dealID, String dealTime, int dealCounterpartyID, int dealInstrumentID, String dealType, float dealAmount, int dealQuantity){
  //       this.dealID = dealID;
  //       this.dealTime = dealTime;
  //       this.dealCounterpartyID = dealCounterpartyID;
  //       this.dealInstrumentID = dealInstrumentID;
  //       this.dealType = dealType;
  //       this.dealAmount = dealAmount;
  //       this.dealQuantity = dealQuantity;
  //   }
  // }



// SQL NEW REQUIREMENTS ONE
public String newRequirementsOne(Connection theConnection){
  String answer = "";

  int dealID;
  float avgBuy;
  float avgSell;

  try{

    String sbQuery = "select deal_instrument_id,sum(case when deal_type = 'B' then deal_amount*deal_quantity else null end)/sum(case when deal_type = 'B' then deal_quantity else null end) as 'Average buy price',sum(case when deal_type = 'S' then deal_amount*deal_quantity else null end)/sum(case when deal_type = 'S' then deal_quantity else null end) as 'Average sell price' from db_grad_cs_1917.deal group by deal_instrument_id;";

    // String sbQuery = "select * from " + dbID + ".deal LIMIT 50" ;

    PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

    ResultSet rs = stmt.executeQuery();

    answer += "[";

    while(rs.next()){

      dealID = rs.getInt("DEAL_INSTRUMENT_ID");
      avgBuy = rs.getFloat("AVERAGE BUY PRICE");
      avgSell = rs.getFloat("AVERAGE SELL PRICE");

      answer += "{\"dealInstrumentID\":" + "\"" + Integer.toString(dealID) + "\", \"averageBuyPrice\" :" + "\"" + Float.toString(avgBuy) + "\",  \"averageSellPrice\":" + "\"" + Float.toString(avgSell) + "\"},";
    }
  }
  catch(SQLException ex){
  }

  // String replace last , with ] to complete JSON format
  StringBuilder b = new StringBuilder(answer);
  b.replace(answer.lastIndexOf(","), answer.lastIndexOf(",") + 1, "]" );
  answer = b.toString();

  return answer;
}




// SQL NEW REQUIREMENTS TWO
public String newRequirementsTwo(Connection theConnection){
  String answer = "";

  int dealCounterpartyID;
  int dealInstrumentID;
  int endingPostion;

  try{

    String sbQuery = "select deal_counterparty_id, deal_instrument_id, (sum(case when deal_type = 'B' then deal_quantity else null end) - sum(case when deal_type = 'S' then deal_quantity else null end)) as position from db_grad_cs_1917.deal group by deal_counterparty_id, deal_instrument_id;";

    // String sbQuery = "select * from " + dbID + ".deal LIMIT 50" ;

    PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

    ResultSet rs = stmt.executeQuery();

    answer += "[";

    while(rs.next()){

      dealCounterpartyID = rs.getInt("DEAL_COUNTERPARTY_ID");
      dealInstrumentID = rs.getInt("DEAL_INSTRUMENT_ID");
      endingPostion = rs.getInt("POSITION");

      answer += "{\"dealCounterpartyID\":" + "\"" + Integer.toString(dealCounterpartyID) + "\", \"dealInstrumentID\" :" + "\"" + Integer.toString(dealInstrumentID) + "\",  \"endingPostion\":" + "\"" + Integer.toString(endingPostion) + "\"},";
    }
  }
  catch(SQLException ex){
  }

  // String replace last , with ] to complete JSON format
  StringBuilder b = new StringBuilder(answer);
  b.replace(answer.lastIndexOf(","), answer.lastIndexOf(",") + 1, "]" );
  answer = b.toString();

  return answer;
}





// public String getDataFromSQLDeal(String dbID, Connection theConnection){
//     String Deals = "";
    
//     int dealID;
//     String dealTime = "";
//     int dealCounterpartyID;
//     int dealInstrumentID;
//     String dealType = "";
//     float dealAmount;
//     int dealQuantity;

//     try{
//         String sbQuery = "select * from " + dbID + ".deal order by deal_time desc LIMIT 10";

//         PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

//         ResultSet rs = stmt.executeQuery();

//         Deals += "[";

//         while(rs.next()){
//           dealID = rs.getInt("DEAL_ID");
//           dealTime = rs.getString("DEAL_TIME");
//           dealCounterpartyID = rs.getInt("DEAL_COUNTERPARTY_ID");
//           dealInstrumentID = rs.getInt("DEAL_INSTRUMENT_ID");
//           dealType = rs.getString("DEAL_TYPE");
//           dealAmount = rs.getFloat("DEAL_AMOUNT");
//           dealQuantity = rs.getInt("DEAL_QUANTITY");

//         Deals += "{\"dealID\":" + "\"" + Integer.toString(dealID) + "\", \"dealTime\" :" + "\"" + dealTime + "\",  \"dealCounterpartyID\":" + "\"" + Integer.toString(dealCounterpartyID) + "\", \"dealInstrumentID\":" + "\"" +  Integer.toString(dealInstrumentID) + "\", \"dealType\":" + "\"" +  dealType + "\", \"dealAmount\":" + "\"" +  Float.toString(dealAmount) + "\", \"dealQuantity\":" + "\"" +  Integer.toString(dealQuantity) + "\"},";


//         // Deals += Integer.toString(dealID) + " , ";

//         }
//     }
//     catch (SQLException ex)
//     {
//     }

//     // String replace last , with ] to complete JSON format
//     StringBuilder b = new StringBuilder(Deals);
//     b.replace(Deals.lastIndexOf(","), Deals.lastIndexOf(",") + 1, "]" );
//     Deals = b.toString();

//     return Deals;
//   }
















 

  public String getDataFromSQLDeal(int pageNumber, String dbID, Connection theConnection){
    String Deals = "";
    
    int dealID;
    String dealTime = "";
    int dealCounterpartyID;
    int dealInstrumentID;
    String dealType = "";
    float dealAmount;
    int dealQuantity;


    // SQL QUERY RANGE

    // int aRange = 0;
    int bRange = 20;

    try{
        String sbQuery = "select * from " + dbID + ".deal LIMIT " + Integer.toString(bRange + 20*(pageNumber-1) ) + ",20;";

        PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

        ResultSet rs = stmt.executeQuery();

        Deals += "[";

        while(rs.next()){
          dealID = rs.getInt("DEAL_ID");
          dealTime = rs.getString("DEAL_TIME");
          dealCounterpartyID = rs.getInt("DEAL_COUNTERPARTY_ID");
          dealInstrumentID = rs.getInt("DEAL_INSTRUMENT_ID");
          dealType = rs.getString("DEAL_TYPE");
          dealAmount = rs.getFloat("DEAL_AMOUNT");
          dealQuantity = rs.getInt("DEAL_QUANTITY");

        Deals += "{\"dealID\":" + "\"" + Integer.toString(dealID) + "\", \"dealTime\" :" + "\"" + dealTime + "\",  \"dealCounterpartyID\":" + "\"" + Integer.toString(dealCounterpartyID) + "\", \"dealInstrumentID\":" + "\"" +  Integer.toString(dealInstrumentID) + "\", \"dealType\":" + "\"" +  dealType + "\", \"dealAmount\":" + "\"" +  Float.toString(dealAmount) + "\", \"dealQuantity\":" + "\"" +  Integer.toString(dealQuantity) + "\"},";


        // Deals += Integer.toString(dealID) + " , ";

        }
    }
    catch (SQLException ex)
    {
    }

    // String replace last , with ] to complete JSON format
    StringBuilder b = new StringBuilder(Deals);
    b.replace(Deals.lastIndexOf(","), Deals.lastIndexOf(",") + 1, "]" );
    Deals = b.toString();

    return Deals;
  }



  public String getDataFromSQLCounterparty(String dbID, Connection theConnection){


    String Counterparties = "";

    int counterpartyID;
    String counterpartyName = "";
    String counterpartyStatus = "";
    String timestamp = "";

    try{
        String sbQuery = "select * from " + dbID + ".counterparty";

        PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

        ResultSet rs = stmt.executeQuery();


        Counterparties += "[";

        while(rs.next()){
          counterpartyID = rs.getInt("COUNTERPARTY_ID");
          counterpartyName = rs.getString("COUNTERPARTY_NAME");
          counterpartyStatus = rs.getString("COUNTERPARTY_STATUS");
          timestamp = rs.getString("COUNTERPARTY_DATE_REGISTERED");
          // Timestamp timestamp = rs.getTimestamp("COUNTERPARTY_DATE_REGISTERED");


        Counterparties += "{\"counterpartyID\":" + "\"" + Integer.toString(counterpartyID) + "\", \"counterpartyName\" :" + "\"" + counterpartyName + "\",  \"counterpartyStatus\":" + "\"" + counterpartyStatus + "\", \"timestamp\":" + "\"" +  timestamp + "\"},";

          // if(rs.first()){
          //     Counterparties += "[";
          //     Counterparties += "{\"counterpartyID\":" + "\"" + Integer.toString(counterpartyID) + "\", \"counterpartyName\" :" + "\"" + counterpartyName + "\",  \"counterpartyStatus\":" + "\"" + counterpartyStatus + "\", \"timestamp\":" + "\"" +  timestamp + "\"},";
          // }
          // else if(rs.last()){
          //     Counterparties += "{\"counterpartyID\":" + "\"" + Integer.toString(counterpartyID) + "\", \"counterpartyName\" :" + "\"" + counterpartyName + "\",  \"counterpartyStatus\":" + "\"" + counterpartyStatus + "\", \"timestamp\":" + "\"" + timestamp + "\"}]";
          // }
          // else{
          //     Counterparties += "{\"counterpartyID\":" + "\"" + Integer.toString(counterpartyID) + "\", \"counterpartyName\" :" + "\"" + counterpartyName + "\",  \"counterpartyStatus\":" + "\"" + counterpartyStatus + "\", \"timestamp\":" + "\"" + timestamp + "\"},";
          // }
          
        }
    }
    catch (SQLException ex)
    {
    }

    // String replace last , with ] to complete JSON format
    StringBuilder b = new StringBuilder(Counterparties);
    b.replace(Counterparties.lastIndexOf(","), Counterparties.lastIndexOf(",") + 1, "]" );
    Counterparties = b.toString();

    return Counterparties;

  }

  public String getCounterparties(){
    String theCounterparties = "";

    theCounterparties = getDataFromSQLCounterparty("db_grad_cs_1917",this.itsConnector.getConnection());

    return theCounterparties;
  }

  public String getDeals(int pageNumber){
    String theDeals = "";

    theDeals = getDataFromSQLDeal(pageNumber,"db_grad_cs_1917",this.itsConnector.getConnection());

    return theDeals;
  }


  public String showTables(Connection theConnection){
    String ShowTables = "";

    String tableName = "";

    try{
        String sbQuery = "SHOW TABLES;";

        PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

        ResultSet rs = stmt.executeQuery();

        ShowTables += "[";

        while(rs.next()){
          tableName = rs.getString("TABLES_IN_DB_GRAD_CS_1917");

          ShowTables += "{\"tableName\":" + "\"" + tableName + "\"},";
        }

    }
    catch (SQLException ex){
    }

    // String replace last , with ] to complete JSON format
    StringBuilder b = new StringBuilder(ShowTables);
    b.replace(ShowTables.lastIndexOf(","), ShowTables.lastIndexOf(",") + 1, "]" );
    ShowTables = b.toString();

    return ShowTables;
  }


  public String getShowTables(){
    String showTableInfo = "";

    showTableInfo = showTables(this.itsConnector.getConnection());

    return showTableInfo;
  }

  public String getNewRequirementsOne(){
    String reqOneInfo = "";

    reqOneInfo = newRequirementsOne(this.itsConnector.getConnection());

    return reqOneInfo;
  }


  public String getNewRequirementsTwo(){
    String reqTwoInfo = "";

    reqTwoInfo = newRequirementsTwo(this.itsConnector.getConnection());

    return reqTwoInfo;
  }

  // public String getNewRequirementsTw(){
  //   String reqOneInfo = "";

  //   reqOneInfo = newRequirementsOne(this.itsConnector.getConnection());

  //   return reqOneInfo;
  // }










  // public ArrayList<Counterparty> getDataFromSQL(String dbID, Connection theConnection){
  //   String sbQuery = "select * from " + dbID + ".counterparty";

  //   PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

  //   ResultSet rs = stmt.executeQuery();


  // }


  // public ArrayList<Counterparty> getDataFromSQLCounterparty(String dbID, Connection theConnection){


  //   ArrayList<Counterparty> Counterparties = null;

  //   int counterpartyID;
  //   String counterpartyName = "";
  //   String counterpartyStatus = "";
  //   String timestamp = "";

  //   try{
  //       String sbQuery = "select * from " + dbID + ".counterparty";

  //       PreparedStatement stmt = theConnection.prepareStatement(sbQuery);

  //       ResultSet rs = stmt.executeQuery();

  //       while(rs.next()){
  //         counterpartyID = rs.getInt("COUNTERPARTY_ID");
  //         counterpartyName = rs.getString("COUNTERPARTY_NAME");
  //         counterpartyStatus = rs.getString("COUNTERPARTY_STATUS");
  //         timestamp = rs.getString("COUNTERPARTY_DATE_REGISTERED");
  //         // Timestamp timestamp = rs.getTimestamp("COUNTERPARTY_DATE_REGISTERED");

  //         Counterparty a = new Counterparty(counterpartyID,counterpartyName,counterpartyStatus,timestamp);

  //         Counterparties.add(a);
          
  //       }

        
  //   }
  //   catch (SQLException ex)
  //   {
  //   }

  //     return Counterparties;

  // }







  
}