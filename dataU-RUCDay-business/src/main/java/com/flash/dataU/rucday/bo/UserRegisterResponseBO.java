package com.flash.dataU.rucday.bo;

import com.flash.dataU.rucday.entity.RucUserDO;
import java.util.List;

/**
 * .
 *
 * @author flash (18811311416@sina.cn)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月26日 10时05分
 */
public class UserRegisterResponseBO {

    private List<IndexResponseBO> indexResponseBOS;
    private RucUserDO userDO;

    @Override
    public String toString() {
        return "UserRegisterResponseBO{" + "indexResponseBOS=" + indexResponseBOS + ", userDO=" + userDO + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserRegisterResponseBO bo = (UserRegisterResponseBO)o;

        if (indexResponseBOS != null ? !indexResponseBOS.equals(bo.indexResponseBOS) : bo.indexResponseBOS != null)
            return false;
        return userDO != null ? userDO.equals(bo.userDO) : bo.userDO == null;
    }

    @Override
    public int hashCode() {
        int result = indexResponseBOS != null ? indexResponseBOS.hashCode() : 0;
        result = 31 * result + (userDO != null ? userDO.hashCode() : 0);
        return result;
    }

    public List<IndexResponseBO> getIndexResponseBOS() {

        return indexResponseBOS;
    }

    public void setIndexResponseBOS(List<IndexResponseBO> indexResponseBOS) {
        this.indexResponseBOS = indexResponseBOS;
    }

    public RucUserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(RucUserDO userDO) {
        this.userDO = userDO;
    }
}
