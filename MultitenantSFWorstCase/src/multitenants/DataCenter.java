/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multitenants;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
//import java.util.Iterator;

/**
 *
 * @author johannes.sianipar
 */
public class DataCenter {
    
    private int id;
    private int highestNumberSameTenant;
    private TreeMap sameTypeTenantNumbers;//= new TreeMap();
    //private int databaseCount;
    //private DBase dataBase []= new DBase [5];
    private List <DBase> dataBase;
    private List <Tenant> tenantInstances;
    
    
    public DataCenter(int identifier){
        this.dataBase = new ArrayList<>();
        this.id=identifier;
        this.highestNumberSameTenant=0;
        this.sameTypeTenantNumbers= new TreeMap();
        //this.databaseCount=0;
        for(int i=0;i<5;i++){
            dataBase.add(new DBase(i));            
        }
        
    }
    public void set_Id(int identifier)
    {
        this.id=identifier;
    }
    public Integer get_Id(){
        return this.id;
    }
    public void set_HighestNumberSameTenant(int number){
        this.highestNumberSameTenant=number;
    }
    public Integer get_HighestNumberSameTenant(){
        return this.highestNumberSameTenant;
    }
    
    /*public void set_databaseCount(int dbc){
        this.databaseCount=dbc;
    }*/
    public Integer get_databaseCount(){
        return this.dataBase.size();
    }
    public List <DBase> getDataBases(){
        return this.dataBase;        
    }    
    public void add_DataBase(DBase db){
        this.dataBase.add(db);        
    }
    public void remove_DataBase(DBase db){
        //db.get_Id();
        //Iterator It= this.dataBase.iterator();
        this.dataBase.remove(db);
    }      
    public void add_SameTypeTenantNumbers(String name, int x){        
        if(this.sameTypeTenantNumbers.containsKey(name)){            
            x=x+(Integer)this.sameTypeTenantNumbers.get(name);
            this.sameTypeTenantNumbers.remove(name);
        }        
        this.sameTypeTenantNumbers.put(name, x);
    }
    public TreeMap get_SameTypeTenantNumber(){
        return this.sameTypeTenantNumbers;
    }
    public void copy_TypeTenantNumber(TreeMap tm){
        this.sameTypeTenantNumbers=tm;
    }
    public void set_TenantInstances(List <Tenant> tI){
        this.tenantInstances=tI;
    }
    public List <Tenant> get_TenantInstances(){
        return this.tenantInstances;
    }
    
}
