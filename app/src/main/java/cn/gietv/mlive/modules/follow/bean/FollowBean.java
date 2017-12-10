package cn.gietv.mlive.modules.follow.bean;

import java.util.List;

/**
 * author：steven
 * datetime：15/9/25 10:58
 *
 */
public class FollowBean {

    /**
     * cnt : 4
     * users : [{"game":5,"myfollow":4,"gender":0,"nickname":"测试用户1","follows":1,"photo":3,"id":"55f26d85aa45eb86c364d191","avatar":"12","video":1},{"game":5,"myfollow":4,"gender":0,"nickname":"测试用户2","follows":1,"photo":3,"id":"55f26d85aa45eb86c364d122","avatar":"12","video":1}]
     */
    private int cnt;
    private List<UsersEntity> users;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public int getCnt() {
        return cnt;
    }

    public List<UsersEntity> getUsers() {
        return users;
    }

    public class UsersEntity {
        /**
         * game : 5
         * myfollow : 4
         * gender : 0
         * nickname : 测试用户1
         * follows : 1
         * photo : 3
         * id : 55f26d85aa45eb86c364d191
         * avatar : 12
         * video : 1
         */
        private int game;
        private int myfollow;
        private int gender;
        private String nickname;
        private int follows;
        private int photo;
        private String id;
        private String avatar;
        private int video;

        public void setGame(int game) {
            this.game = game;
        }

        public void setMyfollow(int myfollow) {
            this.myfollow = myfollow;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setFollows(int follows) {
            this.follows = follows;
        }

        public void setPhoto(int photo) {
            this.photo = photo;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setVideo(int video) {
            this.video = video;
        }

        public int getGame() {
            return game;
        }

        public int getMyfollow() {
            return myfollow;
        }

        public int getGender() {
            return gender;
        }

        public String getNickname() {
            return nickname;
        }

        public int getFollows() {
            return follows;
        }

        public int getPhoto() {
            return photo;
        }

        public String getId() {
            return id;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getVideo() {
            return video;
        }
    }
}
