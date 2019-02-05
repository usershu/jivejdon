package com.jdon.jivejdon.model.message.output.linkurl;

import com.jdon.jivejdon.model.message.MessageUrlVO;
import com.jdon.jivejdon.model.message.MessageVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Quote link
 */
public class LinkUrlExtractor implements Function<MessageVO, MessageVO> {
	private final static Logger logger = LogManager.getLogger(LinkUrlExtractor.class);
	private final static Pattern httpURLEscape = Pattern.compile("^(https?|ftp|file)" +
			"://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");


	@Override
	public MessageVO apply(MessageVO messageVO) {
		String linkUrl = "";
		String newbody;
		Matcher matcher = httpURLEscape.matcher(messageVO.getBody());
		if (matcher.find()) {
			linkUrl = matcher.group();
			newbody = matcher.replaceAll("");
		} else
			newbody = messageVO.getBody();

		messageVO.getForumMessage().setMessageUrlVO(new MessageUrlVO(linkUrl, messageVO
				.getForumMessage().getMessageUrlVO().getThumbnailUrl()));
		return messageVO.builder().subject(messageVO.getSubject()).body(newbody).build();
	}
}
