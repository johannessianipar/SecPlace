/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multitenants;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author johannes.sianipar
 */
public class DBase {
    
    private int id;
    private int tenantCount;
    //private Tenant tenants []= new Tenant [20];
    private List <Tenant> tenants;
    private Integer countBizType []= new Integer[BizType.values().length];
    
    public DBase(int identifier) {
        this.id=identifier;
        this.tenantCount=0;
        this.tenants = new ArrayList<>();
        for (int k=0;k<this.countBizType.length;k++){
            this.countBizType[k]=0;
        }        
    }       
    public void set_Id(int identifier)
    {
        this.id=identifier;
    }
    public Integer get_Id(){
        return this.id;
    }
    public void set_tenantCount(int tc){
        this.tenantCount=tc;
    }
    public Integer get_tenantCount(){
        return this.tenants.size();
    }
    public List <Tenant> get_tenants(){
        return this.tenants;
    }
    public Tenant get_tenant(int sequence){
        return this.tenants.get(sequence);
    }
    public void add_tenant(Tenant tenantInstance){
        //this.tenants[this.tenantCount]=tenantInstance;
        this.tenants.add(tenantInstance);
        //this.tenantCount++;
    }
    public void remove_tenant(Tenant tenantInstance){
        if (!this.tenants.isEmpty()){
            this.tenants.remove(tenantInstance);            
        }
    }
    public void removeall_tenants(){
        while (!this.tenants.isEmpty()){
            this.tenants.remove((this.tenants.size())-1);
        }
    }
    public void setCountBizType(Integer [] cBT){
        this.countBizType=cBT;
    }
    public Integer [] getCountBizType(){        
        return this.countBizType;        
    }
    
}
