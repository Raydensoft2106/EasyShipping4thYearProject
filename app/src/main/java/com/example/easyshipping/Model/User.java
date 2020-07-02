package com.example.easyshipping.Model;

import android.view.Menu;

public class User {
    // the User class has  fields
    //public int id;
    public String fName;
    public String lName;
    public String email;
    public String userType;


    // the User class has one constructor
    public User(/**int id, **/String fName, String lName, String email, String userType) {
        //this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.userType = userType;
    }


    //public int getId(){return id;}

    //public void setId(int id){this.id=id;}

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // derived class
    class WarehouseEmployee extends User {

        // the WarehouseEmployee subclass adds two more fields
        public int scannerId;
        public String depot;

        // the WarehouseEmployee subclass has one constructor
        public WarehouseEmployee(String fName, String lName, String email, String userType,
                            int scannerId, String depot) {
            // invoking base-class(Bicycle) constructor
            super(fName, lName, email, userType);
            this.scannerId=scannerId;
            this.depot=depot;
        }

        public int getScannerId() {
            return scannerId;
        }

        public void setScannerId(int scannerId) {
            this.scannerId = scannerId;
        }

        public String getDepot() {
            return depot;
        }

        public void setDepot(String depot) {
            this.depot = depot;
        }
    }

    class DriverEmployee extends User {

        public int driverId;
        public String depot;

        public DriverEmployee(String fName, String lName, String email, String userType,
                              int driverId, String depot) {

            super(fName, lName, email, userType);
            this.driverId = driverId;
            this.depot = depot;
        }

        public int getDriverId() {
            return driverId;
        }

        public void setDriverId(int driverId) {
            this.driverId = driverId;
        }

        public String getDepot() {
            return depot;
        }

        public void setDepot(String depot) {
            this.depot = depot;
        }

    }

    class SendingCustomer extends User {

        public int sender_id;
        public String SenAddrL1;
        public String SenAddrL2;
        public String SenTwnCty;
        public String SenCntySta;
        public String SenCountry;
        public String SenPostCode;
        public String SenPhoneNo;

        public SendingCustomer(String fName, String lName, String email, String userType,
                               int sender_id, String senAddrL1, String senAddrL2, String senTwnCty, String senCntySta,
                               String senCountry, String senPostCode, String senPhoneNo) {

            super(fName, lName, email, userType);
            this.sender_id = sender_id;
            this.SenAddrL1 = senAddrL1;
            this.SenAddrL2 = senAddrL2;
            this.SenTwnCty = senTwnCty;
            this.SenCntySta = senCntySta;
            this.SenCountry = senCountry;
            this.SenPostCode = senPostCode;
            this.SenPhoneNo = senPhoneNo;
        }

        public int getSender_id() {
            return sender_id;
        }

        public void setSender_id(int sender_id) {
            this.sender_id = sender_id;
        }

        public String getSenAddrL1() {
            return SenAddrL1;
        }

        public void setSenAddrL1(String senAddrL1) {
            SenAddrL1 = senAddrL1;
        }

        public String getSenAddrL2() {
            return SenAddrL2;
        }

        public void setSenAddrL2(String senAddrL2) {
            SenAddrL2 = senAddrL2;
        }

        public String getSenTwnCty() {
            return SenTwnCty;
        }

        public void setSenTwnCty(String senTwnCty) {
            SenTwnCty = senTwnCty;
        }

        public String getSenCntySta() {
            return SenCntySta;
        }

        public void setSenCntySta(String senCntySta) {
            SenCntySta = senCntySta;
        }

        public String getSenCountry() {
            return SenCountry;
        }

        public void setSenCountry(String senCountry) {
            SenCountry = senCountry;
        }

        public String getSenPostCode() {
            return SenPostCode;
        }

        public void setSenPostCode(String senPostCode) {
            SenPostCode = senPostCode;
        }

        public String getSenPhoneNo() {
            return SenPhoneNo;
        }

        public void setSenPhoneNo(String senPhoneNo) {
            SenPhoneNo = senPhoneNo;
        }
    }

    public static class ReceivingCustomer extends User {

        public int receiver_id;
        public String RecAddrL1;
        public String RecAddrL2;
        public String RecTwnCty;
        public String RecCntySta;
        public String RecCountry;
        public String RecPostCode;
        public String RecPhoneNo;
        public String RecHiddenLoc;

        public ReceivingCustomer(String fName, String lName, String email, String userType,
                                 int receiver_id, String recAddrL1, String recAddrL2, String recTwnCty, String recCntySta,
                                 String recCountry, String recPostCode, String recPhoneNo, String recHiddenLoc) {

            super(fName, lName, email, userType);
            this.receiver_id = receiver_id;
            this.RecAddrL1 = recAddrL1;
            this.RecAddrL2 = recAddrL2;
            this.RecTwnCty = recTwnCty;
            this.RecCntySta = recCntySta;
            this.RecCountry = recCountry;
            this.RecPostCode = recPostCode;
            this.RecPhoneNo = recPhoneNo;
            this.RecHiddenLoc = recHiddenLoc;
        }

        public int getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(int receiver_id) {
            this.receiver_id = receiver_id;
        }

        public String getRecAddrL1() {
            return RecAddrL1;
        }

        public void setRecAddrL1(String recAddrL1) {
            RecAddrL1 = recAddrL1;
        }

        public String getRecAddrL2() {
            return RecAddrL2;
        }

        public void setRecAddrL2(String recAddrL2) {
            RecAddrL2 = recAddrL2;
        }

        public String getRecTwnCty() {
            return RecTwnCty;
        }

        public void setRecTwnCty(String recTwnCty) {
            RecTwnCty = recTwnCty;
        }

        public String getRecCntySta() {
            return RecCntySta;
        }

        public void setRecCntySta(String recCntySta) {
            RecCntySta = recCntySta;
        }

        public String getRecCountry() {
            return RecCountry;
        }

        public void setRecCountry(String recCountry) {
            RecCountry = recCountry;
        }

        public String getRecPostCode() {
            return RecPostCode;
        }

        public void setRecPostCode(String recPostCode) {
            RecPostCode = recPostCode;
        }

        public String getRecPhoneNo() {
            return RecPhoneNo;
        }

        public void setRecPhoneNo(String recPhoneNo) {
            RecPhoneNo = recPhoneNo;
        }

        public String getRecHiddenLoc() {
            return RecHiddenLoc;
        }

        public void setRecHiddenLoc(String recHiddenLoc) {
            RecHiddenLoc = recHiddenLoc;
        }
    }
}