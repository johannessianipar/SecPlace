/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multitenants;

/**
 *
 * @author johannes.sianipar
 */
import java.util.ArrayList;
import java.util.List;


public class Tenant {
    
    private int id;
    //String name;
    private String businessType;    
    private String tSize;
    private int probabilityInDatabase;
    private List table;
    private int check;
    private int move;
    private double allocationTime;
    private int numberSameBusinessTypeAndSize;
    
    /*public Tenant(int identifier, BizType type, TenantSize size)
    {
        this.id = identifier;
        this.businessType=type;
        this.tSize=size;
        this.probabilityInDatabase=0;
        //name= Name;
    }*/
    public Tenant(int identifier, String type, String size)
    {
        this.id = identifier;
        this.businessType=type;
        this.tSize=size;
        this.probabilityInDatabase=0;
        this.table= new ArrayList();
        this.check=0;
        this.move=0;
        this.allocationTime=0;
        this.numberSameBusinessTypeAndSize=0;
    }
    public void set_Id(int identifier){
        this.id=identifier;
    }
    public Integer get_Id(){
        return this.id;
    }
    public void set_businessType(String bt){
        this.businessType=bt;
    }
    public String get_businessType(){
        return this.businessType;
    }   
    public void set_tSize(String ts){
        this.tSize=ts;
    }
    public String get_tSize(){
        return this.tSize;
    }
    public void set_probabilityInDatabase(int probability){
        this.probabilityInDatabase= probability;
    }
    public Integer get_probabilityInDatabase(){
        return this.probabilityInDatabase;
    }    
    public void addRecordTable(String text){
        this.table.add(text);        
    }
    public String getRecordTable(int recordNumber){        
        return (this.table.get(recordNumber).toString());        
    }
    public List getTable(){        
        return this.table;        
    }
    public void addCheck(){
        this.check++;        
    }
    public void setCheck(int count){
        this.check=count;        
    }
    public Integer getCheck(){
        return this.check;
    }
    public void addMove(){
        this.move++;
    }
    public Integer getMove(){
        return this.move;
    }    
    public void setAllocationTime(double times){
        this.allocationTime=times;
    }
    public double getAllocationTime(){
        return this.allocationTime;
    }
    public void setNumberSameBusinessTypeAndSize(int number){
        this.numberSameBusinessTypeAndSize=number;
    }
    public void addNumberSameBusinessTypeAndSize(){
        this.numberSameBusinessTypeAndSize++;
    }
    public Integer getNumberSameBusinessTypeAndSize(){
        return this.numberSameBusinessTypeAndSize;        
    }
}
