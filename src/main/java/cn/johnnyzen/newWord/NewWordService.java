package cn.johnnyzen.newWord;

import cn.johnnyzen.user.UserRepository;
import cn.johnnyzen.word.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  18:08:44
 * @Description: ...
 */
@Service("newWordService")
public class NewWordService {
    private static final Logger logger = Logger.getLogger(NewWordService.class.getName());

    @Autowired
    private NewWordRepository newWordRepository;

    @Autowired
    private UserRepository userRepository;

    public Collection<NewWord> findAllByUserId(Integer id){
        return newWordRepository.findAllByUserId(id);
    }

}
