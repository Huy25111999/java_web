package vn.m2m.utils;

public class StaticData {

    public class APIMethod{
        public static final String CREATEACCOUNT = "createaccount";
        public static final String VERIFYAUTHENCODE = "verifyauthencode";
        public static final String RESENDAUTHENCODE = "resendauthencode";
        public static final String DEACTIVEACCOUNT = "deactiveaccount";
        public static final String UPDATEINFO = "updateinfo";
        public static final String CONTACTSYNCFULL = "contactsyncfull";
        public static final String CONTACTSYNCCHANGED = "contactsyncchanged";
        public static final String GETUPDATECONTACTLIST = "getupdatecontactlist";
        public static final String GETSTATUSLIST = "getstatuslist";
        public static final String CHECKVERSION = "checkversion";
        public static final String UPDATESTATUS = "updatestatus";
        public static final String IMPORTCONTACTTOKEN = "setimportcontactpacket";
        public static final String GETCONTACTURL = "getcontacturl";
        public static final String FINISHIMPORTCONTACT = "finishimportcontact";

        public static final String GEN_NEW_DEVKEYID = "generateNewDeviceKeyID";

        //for testing
//        public static final String GENDEVKEY = "gendevkey";
    }

    public static final int MAX_NUMBER_GROUP_CHILD = 5;

    public class SIPContentType{
        public static final String JSON = "application/json";
        public static final String PLAINTEXT = "text/plain";
    }


    public class MobileOS{
        public static final String IOS="ios";
        public static final String ANDROID="android";
    }


    public class AndroidAppVersion{
       //phien ban hien tai moi nhat
        public static final int VERSION_CODE=9;
        public static final String VERSIONNAME="VIP_01_V1.0.0.161130";
        public static final boolean REQUIREUPDATE=true;
        public static final boolean UPDATE_VIA_OTA=false;           //update bang OTA
        public static final String UPDATEURL="https://m2m.vn/ott/Reeng-release-9.apk";
        public static final String UPDATE_FILENAME="Reeng-release-9.apk";
        public static final long UPDATE_FILESIZE=29436702;       //bytes
    }

    public class SIPExtendedHeader{
        public static final String FORWARD = "Forward";
        public static final String CALLERSSRC = "CallerSSRC";
        public static final String CALLEESSRC = "CalleeSSRC";
        public static final String RELAYSERVER = "RelayServer";
        public static final String CALLERIP = "CallerIP";
        public static final String CALLEEIP = "CalleeIP";
        public static final String CALLID = "Call-ID";
        public static final String CONTENTTYPE = "Content-Type";
        public static final String CONTENTLENGTH = "Content-Length";
        public static final String MESSAGEINVITE = "Type";
    }

    public class InViteType{
        public static final String IM = "IM";
        public static final String CALL = "Call";
    }

    public class Presence{
        public static final String STATUS = "Status";
    }



    public class ContactChangedType{
        public static final String ADD = "add";
        public static final String EDIT = "edit";
        public static final String REMOVE = "remove";
    }

    public class HashAlgorithm{
        public static final String SHA256 = "SHA-256";
    }

    public class ClientResponeInvite{
        public static final String INVITING = "inviting";
        public static final String RESPONED = "responed";
    }

    public class FriengNewQueueStatus{
        public static final String NEW = "new";
        public static final String DEACTIVE = "deactive";
    }

    public class SIPStreamDecoder{
        /**
         * The maximum allowed initial line. If we pass this threshold we will drop
         * the messages and close down the connection (if we are using a connection
         * oriented protocol ie)
         */
        public static final int MAX_ALLOWED_INITIAL_LINE_SIZE = 2048;

        /**
         * The maximum allowed size of ALL headers combined (in bytes).
         */
        public static final int MAX_ALLOWED_HEADERS_SIZE = 8192;

        public static final int MAX_ALLOWED_CONTENT_LENGTH = 8192;
    }


    public static final String SIPServerID="server";

    public static final int TimeResetPhoneCreateAccountLock=60*60;		//3 sms / 1 hours			updateprofile
    public static final int MaxResendAuthenCode=3;			//3 times				updateprofile
    public static final int AuthencodeTimeOut=45;           //45 seconds
    public static final int MaxNumberAuthencodeFail=3;           //after 3rd verifying fail -> delete UserTemp

    public static final String VT_Prefix_Number="VIP";

    public static final int NONCETTL=1*6*3600;		//6 hours			Nonce TTL
//    public static final int NONCETTL=5*60;		//5 mins for testing			Nonce TTL

    public static final int TimeToReCreateNONCE=5*60;           //5 mins


    public static final int TimeToScanContact=30;           //30s
    public static final int TimeAssumingOffline=120;           //120

    public static final int TimeToCheckUpdate=6*60*60;           //6h
//    public static final int TimeToCheckUpdate=30;           //30s for testing       //for test

    public static final int INVITE_TIME_REAL_RUN=45; //30 seconds
    public static final int INVITE_TIME_TO_SEND_SMS=15; //15 seconds


    public static final int VERSION_CODE_TO_SEND_SMS=18;    //release 25/04/2017
    public static final int VERSION_CODE_GENERATE_DEVICEKEY_FOR_NON_SECURE_DEVICES=21;    //release 06/05/2017
//    public static final int VERSION_CODE_TO_SEND_SMS=16;    ///for test
    public static final String CONTENT_SMS_WAKEUP="##wakeottup##";    //release 25/04/2017


    public class pagination{
        public static final int ITEM_STATUS_LIST=50; //50 items per page
    }

    public static class OTTAPPUpgradeMode{
        public static final String FULL="FULL";
        public static final String BLACKLIST="BLACKLIST";
        public static final String WHITELIST="WHITELIST";
    }

    public static class OTTAPPTYPE{
        public static final String SMARTPHONE="VIP";
        public static final String FEATUREPHONE="VIPF";
    }

    public static class AppID{
        public static final String OTT_SMARTPHONE="OTT";
        public static final String OTT_FEATUREPHONE="OTT_F";
    }


    public static final String BUILD_MODEL_VIP3_SECURE = "VPH-411s";
    public static final String BUILD_MODEL_VIP3_NON_SECURE = "VPH-411";

    public static final long DEFAULT_RADIUS_ISBUILDING_LBS = 150l; // 150m

    public class HazelcastTTL {
        public static final int SERVERLOG = 1 * 24 * 3600; // 1 day
    }
}
