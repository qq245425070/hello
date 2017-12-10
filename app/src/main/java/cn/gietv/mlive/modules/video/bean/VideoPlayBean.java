package cn.gietv.mlive.modules.video.bean;

import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;

/**
 * Created by houde on 2016/5/29.
 */
public class VideoPlayBean {
    public ProgramBean.ProgramEntity program;
    public List<ProgramBean.ProgramEntity> programlist_album;
    public List<ProgramBean.ProgramEntity> programlist_tag;
    public List<GameInfoBean.GameInfoEntity> albumlist_tag;
}
