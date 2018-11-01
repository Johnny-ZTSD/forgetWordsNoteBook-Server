package cn.johnnyzen.word;

import cn.johnnyzen.user.UserRepository;
import cn.johnnyzen.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  18:07:47
 * @Description: ...
 */
@Service("wordService")
public class WordService {
    private static final Logger logger = Logger.getLogger(WordService.class.getName());

    @Autowired
    private WordRepository wordRepository;
}
