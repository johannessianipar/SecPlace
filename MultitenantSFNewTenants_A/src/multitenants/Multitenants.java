/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multitenants;

/**
 *
 * @author johannes.sianipar
 */
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
//import java.lang.System;




public class Multitenants {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here    
        
        //int d=0;
        
        DataCenter dataCenter= new DataCenter(0);
        DataCenter dataCenter1= new DataCenter(1);
        dataCenter= generateDBasesAndTenants(dataCenter); //read from database files, clean it, and create the linkList of all tenants
        dataCenter1=generateDBasesRandom(dataCenter1,dataCenter.get_TenantInstances());
        //System.out.println("________before placement ________");
        dataCenter1=tenantProbabilityBeforePlacement(dataCenter1);
        dataCenter1=variation(dataCenter1);
        countProbabilityInEachDatabase(dataCenter1,true);        //Need to create the function to compare the table of 2 tenants   
        
        System.out.println("\n_________after placement _________");
        dataCenter= placement(dataCenter);
        dataCenter=variation(dataCenter);
        printMigrationCost(dataCenter);        
        countProbabilityInEachDatabase(dataCenter,true);
        
    }
    
    public static DataCenter generateDBasesAndTenants(DataCenter dc){ 
        
        int k=0,a,b, numSameType=0;
        //Tenant [] tenantInstances = new Tenant[100];
        List <Tenant> tenantInstances= dc.get_TenantInstances();
        String fileName= "src/file/Biz_Size.txt";
        String temp;
        try{
            FileReader fr =new FileReader(fileName);
            BufferedReader textReader;
            textReader=new BufferedReader(fr);
            temp=textReader.readLine();            
            while (temp!=null){
                String [] tempSplit=temp.split("\\s");
                for (int c=0; c<Integer.parseInt(tempSplit[2]);c++){
                    tenantInstances.add(new Tenant(k, tempSplit[0], tempSplit[1]));
                    k++;
                }
                if (Integer.parseInt(tempSplit[2]) > numSameType){
                    //cari highest number of the same type tenants
                    numSameType=Integer.parseInt(tempSplit[2]);
                    
                }
                temp=textReader.readLine();                
            }
            textReader.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        if( numSameType > dc.get_databaseCount()){
            dc.set_HighestNumberSameTenant(numSameType);
        }
        return dc;         
    }
    
    public static DataCenter generateDBasesRandom(DataCenter dc, List <Tenant> tenantInstances ){ 
        
        int k,a, numSameType=0;
                
        List randomTenant= new ArrayList();        
        boolean status;
        for(int i=0;i<dc.get_databaseCount();i++){                        
            //dc.getDataBases()[i]=new DBase(i);             
            for(int j=0;j<20;j++){
                status=true;
                while(status){
                    a= (int) (Math.random()*100);
                    for(k=0;k<randomTenant.size();k++){
                        if(a==randomTenant.get(k)){
                            break;
                        }
                    }
                    if(k==randomTenant.size()){
                        randomTenant.add(a);
                        dc.getDataBases().get(i).add_tenant(tenantInstances.get(a));
                        status=false;
                    }                
                }              
            }     
        } 
        if( numSameType > dc.get_databaseCount()){
            dc.set_HighestNumberSameTenant(numSameType);
        }
        return dc;         
    }
    
    public static void checkExtraDatabaseCost(DataCenter dc){
        double cost= 0;
        int dataBaseCount=dc.get_databaseCount();
        int test= (int)(cost * dataBaseCount);
        //System.out.println("extra "+test);
        for (int i=0;i<test;i++){
            DBase DB= new DBase(dataBaseCount);
            dc.add_DataBase(DB);
            dataBaseCount++;
        }        
        
    }
    
    public static List cleanTenantTable(List Table){
        //find the table of each tenant
        //read the schema
        //remove every column(attribute values) except text or string
        //put it back to the list
        
        List temp= new ArrayList();
        String text;
        for (int i=0; i<Table.size();i++){
            //text= Table;
        }
        
        return Table;
    }
    
    public static DataCenter tenantProbabilityBeforePlacement(DataCenter dc){
        
        for(int i=0;i<5;i++){
            for (int j=0;j<20;j++){
                if (j==0){
                    //high++;
                    dc.getDataBases().get(i).get_tenant(j).set_probabilityInDatabase(10);
                }else{
                    for(int k=0;k<j;k++){
                        if(dc.getDataBases().get(i).get_tenant(j).get_businessType().equals(dc.getDataBases().get(i).get_tenant(k).get_businessType())){
                            if(!dc.getDataBases().get(i).get_tenant(j).get_tSize().equals(dc.getDataBases().get(i).get_tenant(k).get_tSize())){
                                dc.getDataBases().get(i).get_tenant(j).set_probabilityInDatabase(5);
                            }else{
                                dc.getDataBases().get(i).get_tenant(j).set_probabilityInDatabase(1);
                                break;
                            }
                        }
                    }
                    if(dc.getDataBases().get(i).get_tenant(j).get_probabilityInDatabase()==0){
                        dc.getDataBases().get(i).get_tenant(j).set_probabilityInDatabase(10);
                    }               
                }                
            }
        }
        return dc;
    }
    

    
    public static void countProbabilityInEachDatabase(DataCenter dc, boolean afterPlacement){
        int high=0, med=0, low=0, numDB;
        numDB=dc.get_databaseCount();
        
        String fileName= "src/file/probability.txt";
        BufferedWriter writer = null;
        try {            
            File textFile = new File(fileName);
            writer = new BufferedWriter(new FileWriter(textFile, true));
        for(int i=0;i<numDB;i++){
            System.out.println("\n____Database# : "+i);
            writer.write("\n____Database# : "+i+"\n");
            for (int j=0;j<dc.getDataBases().get(i).get_tenantCount();j++){
                
                if (dc.getDataBases().get(i).get_tenant(j).get_probabilityInDatabase()==10){
                    high++;     
                }else{
                    if (dc.getDataBases().get(i).get_tenant(j).get_probabilityInDatabase()==5){
                        med++;                        
                    }else
                    {                        
                        low++;
                    }
                }
            }            
            //highPercent=((double)high/(dc.getDataBases()[i].get_tenantCount()));
            //medPercent=((double)med/dc.getDataBases()[i].get_tenantCount());
            //lowPercent=((double)low/dc.getDataBases()[i].get_tenantCount());
            
            System.out.println("Tenant count "+dc.getDataBases().get(i).get_tenantCount());
            writer.write("Tenant count "+dc.getDataBases().get(i).get_tenantCount()+"\n");
            //System.out.println("High probability: "+high+ "  percentage: "+ highPercent);
            if(afterPlacement){
                System.out.println("High Prob: "+high+ "____________"+ "Middle Prob: "+med+ "___________"+"Low Prob: "+low);
                writer.write("High Prob: "+high+ "____________"+ "Middle Prob: "+med+ "___________"+"Low Prob: "+low+"\n");
            }
            TreeMap tm1=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Enterprise",10);  
            TreeMap tm2=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Medium",10);
            TreeMap tm3=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Small",10);
            
            //System.out.println("Middle Probability: "+med+ "  "+ "(High Pr: "+high+ ", "+"Low Pr: "+low+")");
            TreeMap tm4=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Enterprise",5);
            TreeMap tm5=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Medium",5);
            TreeMap tm6=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Small",5);                       
            
            //System.out.println("Low Probability: "+low+ "  "+ "(High Pr: "+high+ ", "+"Middle Pr: "+med+")");
            TreeMap tm7=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Enterprise",1);
            TreeMap tm8=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Medium",1);            
            TreeMap tm9=displayBiztypeOfProbabilityNSize(dc.getDataBases().get(i),"Small",1);
            
            System.out.println("Enterprise ("+tm1.size()+")___________"+"Enterprise ("+tm4.size()+")___________"+"Enterprise ("+tm7.size()+")          ");
            writer.write("Enterprise ("+tm1.size()+")___________"+"Enterprise ("+tm4.size()+")___________"+"Enterprise ("+tm7.size()+")          \n");
            printArrangement(tm1,tm4,tm7,writer);       
            
            System.out.println("Medium ("+tm2.size()+")_______________"+"Medium ("+tm5.size()+")_______________"+"Medium ("+tm8.size()+")          ");
            writer.write("Medium ("+tm2.size()+")_______________"+"Medium ("+tm5.size()+")_______________"+"Medium ("+tm8.size()+")          \n");
            printArrangement(tm2,tm5,tm8,writer);       
            System.out.println("Small ("+tm3.size()+")________________"+"Small ("+tm6.size()+")________________"+"Small ("+tm9.size()+")          ");
            writer.write("Small ("+tm3.size()+")________________"+"Small ("+tm6.size()+")________________"+"Small ("+tm9.size()+")          \n");
            printArrangement(tm3,tm6,tm9,writer);       
            
            
            //System.out.println("Medium probability: "+med+ " Medium percentage: "+ medPercent);
            //System.out.println("Low probability: "+low+ " low percentage: "+ lowPercent);            
            high=0;
            med=0;
            low=0;
        } 
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {               
                writer.close();
            } catch (Exception e) {
            }
        }
    }
    
    public static void printArrangement(TreeMap tm1, TreeMap tm2,TreeMap tm3, BufferedWriter writer) throws IOException{
            int count=tm1.size();
            if(tm2.size()>count){
                count=tm2.size();
            }
            if(tm3.size()>count){
                count = tm3.size();                        
            }            
            for(int j=0;j<count;j++){
                spaceAlignment(tm1,j, writer);
                spaceAlignment(tm2,j, writer);
                spaceAlignment(tm3,j, writer);
                System.out.print("\n");
                writer.write("\n");
            }       
        
    }
    
    public static void spaceAlignment(TreeMap tm, int seq, BufferedWriter writer) throws IOException{
        int l;
        if (seq < tm.size()){
           String temp=tm.get(seq).toString();                                       
           System.out.print("\033[1m"+tm.get(seq));
           writer.write(tm.get(seq).toString());
           l=25-temp.length();
           while(l!=0){
               System.out.print(" ");
               writer.write(" ");
               l--;
           }
       }else{
           l=25;
           while(l!=0){
               System.out.print(" ");
               writer.write(" ");
               l--;
           }
       }
    }
    
    
    public static TreeMap displayBiztypeOfProbabilityNSize(DBase dB, String tenantSize, int Pr){
        TreeMap tm = new TreeMap();
        int j=0;
        for(int k=0;k<dB.get_tenantCount();k++){
            if(dB.get_tenant(k).get_probabilityInDatabase()==Pr && dB.get_tenant(k).get_tSize().toString().contentEquals(tenantSize)){
                //System.out.println("\033[1m"+dc.getDataBases()[i].get_tenant(k).get_businessType()+ " "+ dc.getDataBases()[i].get_tenant(k).get_tSize());
                tm.put(j, dB.get_tenant(k).get_businessType());
                j++;
            }                
        }
        /*j=0;
        System.out.println(tenantSize+" ("+tm.size()+")");
        while(j < tm.size()){
            System.out.println("\033[1m"+tm.get(j));
            j++;
        } */  
        return tm;        
    }
    
    
    
    public static DataCenter placement(DataCenter dc){
        //DataCenter temporaryDC= new DataCenter(0);
        int j, highestProbability=0, dbHighestProbability=0, numBizSiz, numDB, numDBE;
        long time;
        double timedouble;
                
        //Check for extra cost
        numDB=dc.get_databaseCount();
        if(dc.get_databaseCount()<dc.get_HighestNumberSameTenant()){
            checkExtraDatabaseCost(dc);
        }
        numDBE=dc.get_databaseCount();
        
        //for(int i=0;i<numDB;i++){            
            j=0;
            while (j < dc.get_TenantInstances().size()){                
                numBizSiz=200;
                time=System.nanoTime();                
                for (int a=0;a<numDBE;a++){
                    if(dc.getDataBases().get(a).get_tenantCount()<20){
                        tenantDbaseProbability(dc.getDataBases().get(a), dc.get_TenantInstances().get(j));                                            
                        //System.out.println(dc.getDataBases().get(i).get_tenant(j).get_probabilityInDatabase());
                        if(highestProbability <= dc.get_TenantInstances().get(j).get_probabilityInDatabase()){
                            highestProbability=dc.get_TenantInstances().get(j).get_probabilityInDatabase();
                            dc.get_TenantInstances().get(j).set_probabilityInDatabase(0);
                            
                            if (dc.get_TenantInstances().get(j).getNumberSameBusinessTypeAndSize()==0){
                                dbHighestProbability=a;
                            }else {
                                if (dc.get_TenantInstances().get(j).getNumberSameBusinessTypeAndSize()<numBizSiz){
                                    numBizSiz=dc.get_TenantInstances().get(j).getNumberSameBusinessTypeAndSize();
                                    dc.get_TenantInstances().get(j).setNumberSameBusinessTypeAndSize(0);
                                    dbHighestProbability=a;
                                }
                            }                                                         
                            //dc.getDataBases()[i].get_tenant(j).setNumberSameBusinessTypeAndSize(0);
                            if(highestProbability ==10){
                                break;
                            }
                        }
                    }                                        
                }
                
                //System.out.println(dc.getDataBases().get(i).get_tenant(j).get_businessType()+" "+dc.getDataBases().get(i).get_tenant(j).get_probabilityInDatabase());
                //System.out.println("highest prob "+ highestProbability);
                dc.get_TenantInstances().get(j).set_probabilityInDatabase(highestProbability);                
                dc.get_TenantInstances().get(j).addMove();
                time=System.nanoTime() - time;
                timedouble=((double)time)/1000;
                dc.get_TenantInstances().get(j).setAllocationTime(timedouble);                                                
                //dc.get_TenantInstances().get(j).setAllocationTime(time);
                dc.getDataBases().get(dbHighestProbability).add_tenant(dc.get_TenantInstances().get(j));        
                //System.out.println(time);
                highestProbability=0;                              
                j++;                
            }            
        //}        
        return dc;
    }
    
    public static void tenantDbaseProbability(DBase dBInstance, Tenant tenantInstance){
                        
        tenantInstance.setNumberSameBusinessTypeAndSize(0);
        if(dBInstance.get_tenantCount()==0){
            tenantInstance.set_probabilityInDatabase(10);           
        }else{
            int k=dBInstance.get_tenantCount();
            for(int i=0;i<k;i++){
                tenantInstance.addCheck();
                if(dBInstance.get_tenant(i).get_businessType().equals(tenantInstance.get_businessType())){                    
                    if(!dBInstance.get_tenant(i).get_tSize().equals(tenantInstance.get_tSize())){
                        tenantInstance.set_probabilityInDatabase(5);
                    }else{
                        tenantInstance.set_probabilityInDatabase(1);
                        tenantInstance.addNumberSameBusinessTypeAndSize();
                        //break;
                    }
                }
            }
            if(tenantInstance.get_probabilityInDatabase()==0){
                tenantInstance.set_probabilityInDatabase(10);
            }                
        } 
        //System.out.println(tenantInstance.get_businessType()+" "+tenantInstance.get_probabilityInDatabase()+" "+tenantInstance.getNumberSameBusinessTypeAndSize());
        //return tenantInstance;
    }
    
    public static void printMigrationCost(DataCenter dc){
        //System.out.println("Database ID "+"____________"+ " Tenant ID: "+"___________"+"Check "+"___________"+"Move ");            
        String fileName= "src/file/migrationCost.txt";
        BufferedWriter writer = null;
        int j;
        try {            
            File textFile = new File(fileName);
            writer = new BufferedWriter(new FileWriter(textFile));
            writer.write("DatabaseID "+"TenantID "+"Check "+"Move "+"Time(microsecond)\n");
            
            for(int i=0;i<5;i++){            
            j=0;
                while (j < dc.getDataBases().get(i).get_tenantCount()){                
                    writer.write(dc.getDataBases().get(i).get_Id()+" "+dc.getDataBases().get(i).get_tenant(j).get_Id()+" "+dc.getDataBases().get(i).get_tenant(j).getCheck()+" "+dc.getDataBases().get(i).get_tenant(j).getMove()+" "+dc.getDataBases().get(i).get_tenant(j).getAllocationTime()+"\n");                            
                    j++;                
                }            
            }   
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {               
                writer.close();
            } catch (Exception e) {
            }
        }
    }
    
   public static DataCenter variation(DataCenter dc){
        String fileName= "src/file/variation.txt";
        BufferedWriter writer = null;
            
        for (int k=0; k<dc.getDataBases().size();k++){
            for (int i=0; i< dc.getDataBases().get(k).get_tenantCount();i++){
                for (int j=0;j<BizType.values().length;j++){
                    if(dc.getDataBases().get(k).get_tenant(i).get_businessType().contentEquals(BizType.values()[j].toString())){
                        dc.getDataBases().get(k).getCountBizType()[j]++;
                        break;
                    }                    
                }            
            }
        }
        try {            
            File textFile = new File(fileName);
            writer = new BufferedWriter(new FileWriter(textFile, true));
            //writer.write("DatabaseID "+"TenantID "+"Check "+"Move "+"Time(ns)\n");
        float temp;
        for (int k=0; k<dc.getDataBases().size();k++){
            System.out.println("_____Data base# "+k+ "_____");
            writer.write("_____Data base# "+k+ "_____\n");
            System.out.println("Tenant count "+dc.getDataBases().get(k).get_tenantCount());
            writer.write("Tenant count "+dc.getDataBases().get(k).get_tenantCount()+"\n");
            for (int i=0; i< dc.getDataBases().get(k).getCountBizType().length;i++){
                temp=(float)dc.getDataBases().get(k).getCountBizType()[i]/dc.getDataBases().get(k).get_tenantCount();
                System.out.println(BizType.values()[i].toString()+ ": "+ temp); 
                writer.write(BizType.values()[i].toString()+ ": "+ temp+"\n");
            }
        }   
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {               
                writer.close();
            } catch (Exception e) {
            }
        }
        
        return dc;
    }
        
}
