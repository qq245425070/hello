package cn.gietv.mlive.modules.game.bean;

import cn.gietv.mlive.modules.program.bean.ProgramBean;

import java.util.List;

/**
 * author：steven
 * datetime：15/10/4 10:13
 *
 */
public class GameProgramBean {
    public GameInfoBean.GameInfoEntity game;
    public int cnt;
    public List<ProgramBean.ProgramEntity> programs;
}
