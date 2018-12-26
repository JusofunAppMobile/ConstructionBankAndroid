package sun.bob.mcalendarview.data;

import com.jusfoun.baselibrary.base.BaseModel;
import com.jusfoun.baselibrary.net.NetModel;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:2018/12/1109:12
 * @Email zyp@jusfoun.com
 * @Description ${日历 有跟进记录model}
 */
public class CalendarData extends NetModel {

    public List<MonthDate> datelist;

    public static class MonthDate extends BaseModel {
        public String month;
        public List<DayDate> date;
    }

    public static class DayDate extends BaseModel {
        public String day;
        public boolean statusPhone;
        public boolean statusVisit;
        public boolean statusVisited;
        public boolean statusCooperation;
        public boolean statusformal;
    }
}
