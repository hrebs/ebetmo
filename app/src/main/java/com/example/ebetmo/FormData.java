package com.example.ebetmo;


public class FormData {
    static int i;
    public static String email = "";
    public static String coin = "";
    public static String id = "";

    public static String[] max_bet = {"","","","","","","","","",""};

    public static String[] noSlots = {""};

    public static String getItemId = "0";
    public static String getTempId = "0";
    public static boolean ifRaffle = false;
    public static String choosed = "no";
    public static String winners = "0";
    public static String getItemPrice = "0";
    public static String ID_Type = "";

    public static boolean ifAddedItem = false;

    public static String coinsofUser = "";
    public static String coinsofItem = "";

    public static String imageLocation = "";

    public static String[] verify_info = {   "",  "",   "",  "",  "",  "", "",  "",   "",   ""};
    public static String[] verify_labels = {"fn","age","hs","st","br","ct","pr","zip","typ","pic"};

    public static String[] bet_info = {"","","","","","","",""};
    public static String[] bet_labels = {"itid","oid","img","st","cn","bd","unm","uim"};

    public static String[] profile_info = {"","","","","","","",""};
    public static String[] profile_labels = {"fn","cn","adr","em","pw","pf","vr","bd"};

    public static String[] profile_labels1 = {"fn","cn","adr","em","pw","bd"};

    public static String[] item_info = {    "",   "",   "",  "",  "",  "",  "",  "",  ""};
    public static String[] item_labels = {"itn","itd","iim","tp","tm","pr","sl","wn","st"};

    public static String coin_nm = "cin";
    public static String id_nm = "id";

    public static String[] id1 ={""};
    public static String[] itemName ={""};
    public static String[] des ={""};
    public static String[] image1 ={""};
    public static String[] date ={""};
    public static String[] price1 ={""};
    public static String[] slots1 ={""};
    public static String[] stats ={""};
    public static String[] win ={""};
    public static String[] type1 ={""};

    public static String[] user_id ={""};
    public static String[] user_name ={""};
    public static String[] user_image ={""};

    public static String[] user_info = {"","",""};

    public static String[] owner_image1 ={""};
    public static String[] owner_name1 ={""};
    public static String[] slot_number ={""};
    public static String[] item_id1 ={""};

    public static String[] changeName ={""};
    public static String[] changeImage ={""};

    public static void clearPF(){
        for(i=0;i<profile_info.length;i++){
            profile_info[i] ="";
        }
        email ="";
        id ="";
        coin="";
    }

    public static void clearItem(){
        for(i=0;i<item_info.length;i++){
            item_info[i] ="";
        }
    }
}
