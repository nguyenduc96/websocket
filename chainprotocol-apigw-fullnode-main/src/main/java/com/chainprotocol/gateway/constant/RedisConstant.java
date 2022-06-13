package com.chainprotocol.gateway.constant;

public class RedisConstant {

    public static final class DB {
        public static final class REGISTER {    
            public static final int INDEX = 0;
            public static final String CONNECTION_FACTORY = "registerConnectionFactory";
            public static final String REDIS_TEMPLATE = "registerRedisTemplate";
            public static final class KEY_CONCAT {
                public static String ENDPOINTS(String type) { return "GW_ENDPOINTS_" + type; } // /api/gw
            }
        }
        public static final class CACHE {
            public static final int INDEX = 3;
            public static final String CONNECTION_FACTORY = "cacheConnectionFactory";
            public static final String REDIS_TEMPLATE = "cacheRedisTemplate";
            public static final class KEY_PREFIX {
                public static final String PAYMENT = "PAYMENT_";
                public static final String THREE_DS = "3DS_";
                public static final String OTP = "OTP_";
                public static final String REQ = "REQ_";
            }
            public static final class EXPIRE {
                public static final int PAYMENT = 10 * 60;
                public static final int THREE_DS = 30 * 60;
                public static final int OTP = 20 * 60;
                public static final int REQ = 30 * 60;
            }
        }
        public static final class GW_MAPPING {
            public static final int INDEX = 2;
            public static final String CONNECTION_FACTORY = "gwMappingConnectionFactory";
            public static final String REDIS_TEMPLATE = "gwMappingRedisTemplate";

            public GW_MAPPING() {
            }

            public static final class TIMEOUT {
                public static final int MCPTID_REF_NO_T_TXN_ID = 180;
                public static final int REF_NO_T_MPI_TXN_ID = 30;
                public static final int ORI_ID_T_TXN_ID = 180;

                public TIMEOUT() {
                }
            }

            public static final class KEY_CONCAT {
                public KEY_CONCAT() {
                }

                public static String MCPTID_REF_NO_T_TXN_ID(String mcpTerminalId, String refNo) {
                    return "T_MT_RN_TI_" + mcpTerminalId + "_" + refNo;
                }

                public static String REF_NO_T_MPI_TXN_ID(String refNo) {
                    return "T_RN_MPITI_" + refNo;
                }

                public static String ORI_ID_T_TXN_ID(Long oriId) {
                    return "T_OI_TI_" + oriId;
                }
            }
        }
    }
}
