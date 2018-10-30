package com.youmeng.taoshelf.service;

import com.youmeng.taoshelf.entity.Card;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.repository.CardRepository;
import com.youmeng.taoshelf.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Transactional
@Service
public class CardService {

    @Resource
    private CardRepository cardRepository;

    @Resource
    private UserRepository userRepository;

    public boolean useCard(String id, User user) {
        try {
            Card card = cardRepository.findCardById(id);
            if (card == null) {
                return false;
            } else if (card.getUser() != null) {
                return false;
            } else {
                Date endTime = user.getEndTime();
                Date currentDate = new Date();
                Calendar calendar = new GregorianCalendar();
                if (endTime.after(currentDate)) {
                    calendar.setTime(endTime);
                } else {
                    calendar.setTime(currentDate);
                }
                calendar.add(Calendar.DATE, card.getDay());
                user.setEndTime(calendar.getTime());
                card.setUser(user);
                card.setUsedTime(new Date());
                userRepository.save(user);
                cardRepository.save(card);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public Page<Card> getAllNewCard(int size, int no) {
        Pageable pageable = PageRequest.of(no, size);
        return cardRepository.findCardsByUserIsNull(pageable);
    }

    public Page<Card> getAllOldCard(int size, int no) {
        Pageable pageable = PageRequest.of(no, size);
        return cardRepository.findCardsByUserIsNotNullAndUsedTimeIsNotNull(pageable);
    }

    public void importCards(List<Card> cards) {
        cardRepository.saveAll(cards);
    }

    public int deleteCardById(String[] ids) {
        return cardRepository.deleteCardsByIdIn(ids);
    }

    @Cacheable(cacheNames = "card.all")
    public List<Card> getAllCard() {
        return cardRepository.findAll();
    }

}
