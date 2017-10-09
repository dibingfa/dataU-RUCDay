package com.flash.dataU.rucday.util;

/**
 * .
 *
 * @author flash (18811311416@sina.cn)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月06日 17时34分
 */
public enum ErrorCodeEnum {

    SUCCESS(200),

    RequiredParameterMissing(401),

    ParameterError(402),

    ServiceDeprecated(404),

    ERROR(500),

    THIRD_PARTY_TIMEOUT(504),

    ZOOKEEPER_ERROR(10000001),

    OUT_OF_PAGE_ERROR(10000002),

    OBJECT_NOT_EXIST(10000003),

    DUPLICATE_CREATE_OBJECT(10000004),

    UNSUPPORTED_TIME_PERIOD(10000005),

    PARAMETER_NOT_MATCH(10000006),

    ACCOUNT_ERROR(10010001),

    ACCOUNT_STATUS_ERROR(10010002),

    ACCOUNT_BALANCE_ERROR(10010003),

    ACCOUNT_LOCKED_BALANCE_ERROR(10010004),

    TRANSACTION_ERROR(10020001),

    TRANSACTION_STATUS_ERROR(10020002),

    REPAYMENT_GREATER_THAN_ARREARS(10030001),

    USER_STATUS_ERROR(20000001),

    TOKEN_STATUS_ERROR(20000002);

    int value;

    private ErrorCodeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
