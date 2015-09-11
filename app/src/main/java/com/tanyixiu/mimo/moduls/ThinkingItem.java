package com.tanyixiu.mimo.moduls;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.tanyixiu.mimo.utils.StringHelper;

import java.util.UUID;

/**
 * Created by Mimo on 2015/9/10.
 */
@Table(name = "thinkingitem", id = "tb_thinkingitem_id")
public class ThinkingItem extends Model {

    @Column(name = "id", notNull = true, unique = true)
    private String id;

    @Column(name = "idea", notNull = true)
    private String idea;

    @Column(name = "ismark", notNull = true)
    private boolean isMark;

    @Column(name = "createtime")
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public boolean getIsMark() {
        return isMark;
    }

    public void setIsMark(boolean isMark) {
        this.isMark = isMark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static ThinkingItem createItem(String idea) {
        ThinkingItem item = new ThinkingItem();
        item.setId(UUID.randomUUID().toString());
        item.setIdea(idea);
        item.setIsMark(false);
        item.setCreateTime(StringHelper.getCurrentTime());
        return item;
    }
}
