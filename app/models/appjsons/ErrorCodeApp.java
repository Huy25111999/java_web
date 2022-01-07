package models.appjsons;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCodeApp {
    //no error
    Ok(0),
    // common 10xxx
    UnknowError(10000),
    InvalidParams(10001),
    DataNotFound(10002),
    InvalidImageType(10003),
    ParserEror(10004),
    // authen 11xxx
    SessionExpired(11001),
    EmptyEmail(11002),
    EmptyPass(11003),
    EmptyRePass(11004),
    RePassNotMatch(11005),
    InvalidEmail(11006),
    InvalidEmailPass(11007),
    EmailExisted(11008),
    EmailNotExisted(11009),
    PhoneExisted(11010),
    PhoneValidated(11011),
    PhonelockValid(11012),
    TimeResendCodeFailed(11013),
    OverTimeResendCode(11014),
    EmptyAuthenCode(11015),
    WrongAuthenCode(11016),
    AuthencodeTTLFailed(11017),
    OverTimeSendAuthenCode(11018),
    EmptyId(11019),
    UserTempNotFound(11020),
    PhoneNotExisted(11021),
    InvalidPassFormat(11022),
    EmptyAccessToken(11023),
    EmptySocialId(11024),
    EmptySocialAvatar(11025),
    EmptySocialName(11026),
    EmptySocialType(11027),
    CannotGetFBInfo(11028),


    // profile 12xxx
    EmptyUserName(12001),
    OldPassNotMatch(12002),
    InvalidNewPass(12003),

    // admin 13xxx
    InvalidAdminAuth(13001),
    EmptyCachedName(13002),
    WrongCachedName(13003),
    // device 21xxx
    EmptyImei(21001),
    ImeiNotFound(21002),
    DeviceGpsNotFound(21003),
    DeviceInfoNotFound(21004),
    EmptyDeviceName(21005),
    EmptyAvatar(21006),
    EmptyIntervalConnect(21007),
    EmptyToken(21009),
    EmptyPhoneNumber(21008),
    InvalidToken(21010),
    EmptyUsrID(21011),
    DeviceInUserdevice(21012),
    DevNotBelongToUser(21013),
    EmptyImeiTokenPaircode(21014),
    InvalidPairingCode(21015),
    PairingCodeExpired(21016),
    InvalidPhoneNumber(21017),
    PhoneNumberNotKid(21018),
    DontNeedValid(21019),
    CheckPhoneNotFound(21020),
    PhoneNeedValidation(21021),
    KeyNotFound(21022),



    //map 30xxx
    EmptyDeviceList(30000),
    EmptyLBSOption(30002),
    //history 31xxx
    EmptyHistoryDeviceID(31000),
    EmptyHistoryDate(31001),
    EmptyHistoryTimeFrom(31002),
    EmptyHistoryTimeTo(31003),
    WrongFormatTime(31004),

    //Safezone 40xx
    DeviceSafezoneNotFound(40000),
    EmptySafezoneName(40001),
    EmptySafezonecenterLat(40002),
    EmptySafezonecenterLon(40003),
    EmptySafezonecenterradius(40004),
    EmptySafezonecenterTimeFrom(40005),
    EmptySafezonecenterTimeTo(40006),
    EmptySafezonedays(40007),
    EmptySafezoneID(40008),
    SafezoneOversize(40009),
    SafezoneNotFound(40010),
    EmptyNotifySMS(40011),
    EmptyNotifyEmail(40012),
    EmptyNotifyApp(40013),
    EmptyNumberNotify(40014),

    //Phonebook 50xx
    DeviceVcardNotFound(50000),
    EmptyVcardFN(50001),
    EmptyVcardCellnum(50002),
    EmptyVcardRole(50003),
    VcardOversize(50004),
    VcardNotFound(50005),
    EmptyVcardID(50006),
    //Calendar 60xxx
    CalNotBelongToUser(60000),
    MaxNumberOfEvent(60001),
    ActionEventFormat(60002),
    DayOfWeekEmpty(60003),
    IdEventEmpty(60004),
    TitleEventEmpty(60005),
    WrongFormatRuleEvent(60006),
    WrongFormatDateEvent(60007),
    WrongFormatDayOfWeek(60008),
    IdEventNotFound(60009),
    StartTimeEventNotFound(60010),
    EndTimeEventNotFound(60011),
    ActionEventEmpty(60012),
    RuleEventEmpty(60013),
    EventNotOneDay(60014),
    ColorEventEmpty(60015),
    WrongColorEvent(60016),
    //Calendar 70xxx
    EmptyPushtokenID(70000),
    EmptyOS(70001),
    EmptyOSver(70002),
    EmptySW(70003),

    //Viettel Mobile 80xxx
    PhonenumNotFound(80001),
    EmptyPaycardnum(80002),
    //SMS Control
    EmptyType(90001),
    OverUsageAMinute(90002),
    OverUsagePerHour(90003),
    OverUsagePerDay(90004),

    //DeviceNotificationInfo
    Emptypage(100001),
    Emptypagesize(100002)
    ;


    private final int code;

    private static final Map<Integer, ErrorCodeApp> errorByCode = new HashMap<Integer, ErrorCodeApp>();

    static {
        for(ErrorCodeApp errorCode: ErrorCodeApp.values()){
            errorByCode.put(errorCode.getCode(),errorCode);
        }
    }

    ErrorCodeApp(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public String getName(){
        return name();
    }
    public boolean isError(){
        return (this.code != 0);
    }

    public int getStatus(){
        //1: ok, 0: fail
        return (this.code == 0)? 1 : 0;
    }

    public static ErrorCodeApp fromCode(int code){
        return errorByCode.get(code);
    }
}
