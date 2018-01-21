package com.houde.competitive.lagua.model.recommend;

import com.houde.competitive.lagua.contrart.recommend.SatinsContract;
import com.houde.competitive.lagua.model.bean.SatinsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class SatinsModel implements SatinsContract.ISatinsModel {
    public static SatinsModel newInstance(){
        return new SatinsModel();
    }
    private Random mRandom;
    public SatinsModel(){
        mRandom = new Random();
    }
    @Override
    public List<SatinsBean> getData() {
        List<SatinsBean> beanList = new ArrayList<>();
        beanList.add(createSatins(SatinsBean.TYPE_HEADER));
        for(int i = 0 ; i < 6 ; i++){
            beanList.add(createSatins(SatinsBean.TYPE_CONTENT));
        }
        return beanList;
    }
    private SatinsBean createSatins(int type){
        SatinsBean bean = new SatinsBean();
        switch (type){
            case SatinsBean.TYPE_HEADER:
                bean.type = SatinsBean.TYPE_HEADER;
                break;
            case SatinsBean.TYPE_CONTENT:
                bean.type = SatinsBean.TYPE_CONTENT;
                bean.id = mRandom.nextInt(100);
                bean.content = getSatins();
                bean.shareCount = 100;
                bean.upvoteCount = 1000;
                bean.caiCount = 100;
                break;
        }
        return bean;
    }
    private String getSatins(){
        String[] strings = new String[10];
        strings[0] = "一哥们问一位快要奔三的美女，“你这么漂亮咋还没结婚？”美女说：“我小时候削苹果，不小心削到手，到现在还有一道疤呢！”哥们不解：“手上的疤痕，跟结婚有啥关系啊？”美女回答：“你说呢？”";
        strings[1] = "老公有些色弱。红绿不分。一天晚上，我拿着报纸念给他听：“据调查研究表明，69%的男人拥有红颜知己……”念到这，老公笑了，说：“这纯属胡扯！”我开玩笑说：“老实交待，你有没有红颜知己啊？”老公笑嘻嘻地说：“你总说我红绿不分，即使有‘红’颜知己，我也看不出来啊！”";
        strings[2] = "一同事家庭条件不错，人也帅，只是忙于工作一直单身。今天他去相亲，公司里的已婚少妇暗暗嫉妒那幸运女孩…谁知道他去了不到半小时就黑着脸回来了，我们奇怪问他怎么了？他:“我…我不嫌她丑，她却赶我走，说我人太帅，相亲是借口…”";
        strings[3] = "晚上叫的外卖，正洗澡时电话响了，让我去楼下拿，我说没空，本来想让他放一楼的小卖部里，结果他说给我送上来，于是我说:那麻烦你去帮我把一楼小卖部里的其他两个包裹一起带上来吧！对方电话直接挂了…正郁闷，外卖小哥气喘吁吁的出现在我的面前了……";
        strings[4] = "人一辈子很短，余额不足可以挣，电量不足可以充，时间走了就回不来了，所以，身边的人们要相互珍惜，因为每个人的时间越來越少……不要争执，不要斗气，好好说话，相互理解，善待亲人，珍惜每一份真感情，下辈子未必能遇上……一辈子要记住三句话，看人长处，帮人难处，记人好处！";
        strings[5] = "老婆性欲太强了 怎么办？每天下班回家都够累的，每个星期都最少4天，一天就三次！唉！已经严重影响到我上班了，和老婆协商也无果！段友给我出出招？？";
        strings[6] = "呵呵呵，老子送个外卖这么冷的天下着雨辛辛苦苦赚了这点钱。你既然把我俩辆吃饭的东西都偷了！呵呵呵，真是够了。老子快崩溃了，偷的都是没钱人的，不会觉得惭愧？内疚？老子没有钱，就当我送给你们家买棺材的吧！";
        strings[7] = "跟女神吃饭，到了很晚，女神羞涩地说：“今晚想加水变氢氧化钙！”我顿时心花怒放喜在心头：“老板结账，快，赶紧的！”化学学得不好！大神解释下";
        strings[8] = "当眼泪掉下来的时候，是真的累了， 其实人生就是这样: 你有你的烦，我有我的难，人人都有无声的泪，人人都有难言的苦。 忘不了的昨天，忙不完的今天，想不到的明天，走不完的人生，过不完的坎坷，越不过的无奈，听不完的谎言，看不透的人心放不下的牵挂，经历不完的酸甜苦辣，这就是人生，这就是生活。";
        strings[9] = "有钱的女人看鞋，风流女人看指甲，性感女人看香水，气质女人看手表，拜金女人看包包，贤惠女人看饭菜，浪漫女人看睡衣。 我看完后，发现我好像不是女人，赶紧掏出身份证一看，性别：女，心里才踏实了些，太不容易了，一样没占。";
        return strings[mRandom.nextInt(10)];
    }
}
