package bank.pkobp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest2 {

    private int version;

    private int seq;

    private String location;

    @JsonProperty("state_id")
    private String stateId;

    @JsonProperty("flow_id")
    private String flowId;

    private String token;

    private Data data;

    private String action;

    public LoginRequest2(LoginResponse loginResponse, String password) {
        this.version = 3;
        this.seq = 1;
        this.location = "";
        this.stateId = "password";
        this.data = new Data(password, loginResponse.getSessionUUID());
        this.flowId = loginResponse.getFlowId();
        this.token = loginResponse.getToken();
        this.action = "submit";
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Data {
        private String password;
        @JsonProperty("widget_data")
        private String widgetData;
        private String placement;
        @JsonProperty("placement_page_no")
        private Integer placementPageNo;
        @JsonProperty("session_uuid")
        private String sessionUUID;
        @JsonProperty("behavioral_tracking_id")
        private String behavioralTrackingId;

        public Data(String password, String sessionUUID) {
            this.password = password;
            this.widgetData = "\"{\\\"jvqtrgQngn\\\":{\\\"oq\\\":\\\"1080:719:1080:1920:1080:1920\\\",\\\"wfi\\\":\\\"flap-1\\\",\\\"ji\\\":\\\"2.3.1\\\",\\\"oc\\\":\\\"2501pp0s72219oop\\\",\\\"fe\\\":\\\"1080k1920 24\\\",\\\"qvqgm\\\":\\\"-60\\\",\\\"jxe\\\":252820,\\\"syi\\\":\\\"snyfr\\\",\\\"si\\\":\\\"si,btt,zc4,jroz\\\",\\\"sn\\\":\\\"sn,zcrt,btt,jni\\\",\\\"us\\\":\\\"rq0s95p57n0r1232\\\",\\\"cy\\\":\\\"Yvahk k86_64\\\",\\\"sg\\\":\\\"{\\\\\\\"zgc\\\\\\\":0,\\\\\\\"gf\\\\\\\":snyfr,\\\\\\\"gr\\\\\\\":snyfr}\\\",\\\"sp\\\":\\\"{\\\\\\\"gp\\\\\\\":gehr,\\\\\\\"ap\\\\\\\":gehr}\\\",\\\"sf\\\":\\\"gehr\\\",\\\"jt\\\":\\\"n46p01n68sp5740r\\\",\\\"sz\\\":\\\"p5pp429p6n50s6s\\\",\\\"vce\\\":\\\"apvc,0,667p986n,2,1;fg,0,258nr7po-57n1-4r7s-8910-p8oo425n764p,0;ss,0,258nr7po-57n1-4r7s-8910-p8oo425n764p;zz,536,111,152,;so,361,258nr7po-57n1-4r7s-8910-p8oo425n764p;zzf,87,0,n,75 0,qos 212n,n55,n96,-n596,389n,-2osp;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,3r8,3r8,n,ABC;zzf,2711,2711,32,ABC;gf,0,5357;zzf,270s,270s,32,ABC;zzf,2711,2711,32,ABC;gf,0,n177;zzf,2710,2710,32,ABC;zzf,2710,2710,32,ABC;gf,0,rs97;zz,12p4,3qs,2po,;zzf,144p,2710,32,704r 2os,704r 2os,23s,1678,-231o3,231o3,0;zzf,rn60,rn60,1r,ABC;gf,0,20107;zzf,rn60,rn60,1r,ABC;gf,0,2ro67;zzf,rn60,rn60,1r,ABC;gf,0,3q5p7;zzf,rn60,rn60,1r,ABC;gf,0,4p027;zzf,rn60,rn60,1r,ABC;gf,0,5nn87;zz,10s8s,3s0,0,;gf,0,6on16;xx,psq,0,258nr7po-57n1-4r7s-8910-p8oo425n764p;ss,0,258nr7po-57n1-4r7s-8910-p8oo425n764p;zp,62,os,118,258nr7po-57n1-4r7s-8910-p8oo425n764p;xq,214,0;xh,88,0;xq,o2,0;xh,81,0;xq,121,0;xh,53,0;xq,50,0;xh,6q,0;xq,4r,0;xh,67,0;xq,5r,0;xh,6p,0;xq,1,0;xq,6p,1;xh,n,0;xh,6q,1;xq,123,0;zp,2,0,0,;xh,n7,0;so,2s,258nr7po-57n1-4r7s-8910-p8oo425n764p;fg,4,31345o6p-643n-4q1p-88p3-35psr163095r,0;zz,286,os,118,;so,454,31345o6p-643n-4q1p-88p3-35psr163095r;zz,65p5,97,2pq,;gf,0,73r76;zz,5072,18q,2pq,;gf,0,78rr8;xx,2p1,0,31345o6p-643n-4q1p-88p3-35psr163095r;ss,0,31345o6p-643n-4q1p-88p3-35psr163095r;zp,6p,q9,146,31345o6p-643n-4q1p-88p3-35psr163095r;xq,306,0,4;xh,8q,0;xq,5p,0;xq,6o,1,4;xh,1,0;xh,7o,1;xq,n3,0;xh,72,0;xq,92,0,5;xq,ps,1;xh,59,1;xq,5q,1;xh,84,0;xh,10;xq,12p,1;xh,73,1;xq,20,1;xh,7n,1;xq,r,1;xh,72,1;xq,o2,1;xh,74,1;xq,pp,1;xh,56,1;xq,pn,1;xh,6p,1;xq,5p,1,5;xq,ss,2;xh,np,2;xh,1,1;zz,215,q9,146,31345o6p-643n-4q1p-88p3-35psr163095r;so,4p8,31345o6p-643n-4q1p-88p3-35psr163095r;zp,99,16p,18s,;\\\",\\\"vp\\\":\\\"0,qr;\\\",\\\"ns\\\":\\\"\\\",\\\"qvg\\\":\\\"\\\"},\\\"jg\\\":\\\"\\\"}\"";
            this.placement = "LoginPKO";
            this.placementPageNo = 1;
            this.sessionUUID = sessionUUID;
            this.behavioralTrackingId = "AjMrzGITeOwAAAGQVrNZ0LVoa4FyzbK0M0CmlmvfMM70uXUqSk34uiBK2uSW5glL";
        }
    }

}