package com.kn.elephant.note.notification;

import java.util.function.BiConsumer;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by Kamil Nadłonek on 02-04-2017 email:kamilnadlonek@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class ActionNoticeData extends NoticeData {

    public static final String ACTION_NOTICE_TYPE = "ACTION_NOTICE";
    private boolean actionFire;
    private NoteDto noteDto;
    private String buttonName;
    private NotificationAction buttonAction;
    private BiConsumer<NoteDto, Boolean> hideAfterAction;

    public ActionNoticeData(String message) {
        super(message);
        setDisplayTime(7000);
        actionFire = false;
    }

    public void callAction() {
        if (buttonAction == null) {
            throw new IllegalArgumentException("Action for button is no define!");
        }
        buttonAction.call();
        actionFire = true;
    }

    public void callHideAction() {
        if (hideAfterAction == null) {
            throw new IllegalArgumentException("Hide action is no define!");
        }
        hideAfterAction.accept(noteDto, actionFire);
    }

    @Override
    public String getType() {
        return ACTION_NOTICE_TYPE;
    }
}
