package com.flash.dataU.rucday.bo;

import java.util.List;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月27日 11时11分
 */
public class GroupDetailResponseBO {

    private List<GroupMessageDetailResponseBO> groupMessageDetailResponseBOS;

    private int index;

    @Override
    public String toString() {
        return "GroupDetailResponseBO{" + "groupMessageDetailResponseBOS=" + groupMessageDetailResponseBOS + ", index=" + index + '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GroupDetailResponseBO that = (GroupDetailResponseBO)o;

        if (index != that.index)
            return false;
        return groupMessageDetailResponseBOS != null ? groupMessageDetailResponseBOS.equals(that.groupMessageDetailResponseBOS) : that.groupMessageDetailResponseBOS == null;
    }

    @Override
    public int hashCode() {
        int result = groupMessageDetailResponseBOS != null ? groupMessageDetailResponseBOS.hashCode() : 0;
        result = 31 * result + index;
        return result;
    }

    public List<GroupMessageDetailResponseBO> getGroupMessageDetailResponseBOS() {

        return groupMessageDetailResponseBOS;
    }

    public void setGroupMessageDetailResponseBOS(List<GroupMessageDetailResponseBO> groupMessageDetailResponseBOS) {
        this.groupMessageDetailResponseBOS = groupMessageDetailResponseBOS;
    }
}
