package com.youmeng.taoshelf.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.entity.Result;
import com.youmeng.taoshelf.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "client1")
    private TaobaoClient client1;

    @Resource(name = "client2")
    private TaobaoClient client2;

    @Resource
    private LogService logService;

    //获取在售
    public Result<Good> getGoodsOnsale(User user, String q, Long page_size, Long page_no, int flag) {
        Result<Good> result = new Result<>();
        ItemsOnsaleGetRequest request = new ItemsOnsaleGetRequest();
        request.setFields("num_iid,title,approve_status,num,modified,delist_time,list_time");
        request.setQ(q);
        request.setPageSize(page_size);
        request.setPageNo(page_no);
        ItemsOnsaleGetResponse response;
        try {
            if (flag == 1) {
                response = client1.execute(request, user.getSessionKey1());
            } else {
                response = client2.execute(request, user.getSessionKey2());
            }
            Long total_results = JSON.parseObject(response.getBody()).getJSONObject("items_onsale_get_response").getLong("total_results");
            List<Good> goodList = new ArrayList<>();
            if (total_results > 0) {
                JSONArray jsonArray = JSON.parseObject(response.getBody()).getJSONObject("items_onsale_get_response").getJSONObject("items").getJSONArray("item");
                for (Object item : jsonArray) {
                    Good goods = JSON.parseObject(item.toString(), Good.class);
                    goodList.add(goods);
                }
            }
            result.setTotal(total_results);
            result.setItems(goodList);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    //获取库存
    public Result<Good> getGoodsInstock(User user, String q, Long page_size, Long page_no, int flag) {
        Result<Good> result = new Result<>();
        ItemsInventoryGetRequest request = new ItemsInventoryGetRequest();
        request.setFields("num_iid,title,approve_status,num,modified,delist_time,list_time");
        request.setQ(q);
        request.setPageSize(page_size);
        request.setPageNo(page_no);
        ItemsInventoryGetResponse response;
        try {
            if (flag == 1) {
                response = client1.execute(request, user.getSessionKey1());
            } else {
                response = client2.execute(request, user.getSessionKey2());
            }
            Long total_results = JSON.parseObject(response.getBody()).getJSONObject("items_inventory_get_response").getLong("total_results");
            List<Good> goodList = new ArrayList<>();
            if (total_results > 0) {
                JSONArray jsonArray = JSON.parseObject(response.getBody()).getJSONObject("items_inventory_get_response").getJSONObject("items").getJSONArray("item");
                for (Object item : jsonArray) {
                    Good good = JSON.parseObject(item.toString(), Good.class);
                    goodList.add(good);
                }
            }
            result.setTotal(total_results);
            result.setItems(goodList);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    //上架
    public boolean doGoodListing(User user, Good good, int flag) {
        ItemUpdateListingRequest request = new ItemUpdateListingRequest();
        request.setNumIid(good.getNumIid());
        request.setNum(good.getNum());
        ItemUpdateListingResponse response;
        try {
            if (flag == 1) {
                response = client1.execute(request, user.getSessionKey1());
            } else {
                response = client2.execute(request, user.getSessionKey2());
            }
            if (response.getBody().contains("item_update_listing_response")) {
                good.setApproveStatus("onsale");
                return true;
            } else {
                if (!response.getErrorCode().equals("7") && !response.getErrorCode().equals("530")) {
                    logService.log(user, response.getErrorCode() + " " + response.getMsg(), response.getSubMsg());
                } else {
                    logger.debug(user.getNick() + ":" + flag + " " + response.getErrorCode() + " " + response.getMsg() + " " + response.getSubCode() + " " + response.getSubMsg());
                }
                return false;
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    //下架
    public boolean doGoodDelisting(User user, Good good, int flag) {
        ItemUpdateDelistingRequest request = new ItemUpdateDelistingRequest();
        request.setNumIid(good.getNumIid());
        ItemUpdateDelistingResponse response;
        try {
            if (flag == 1) {
                response = client1.execute(request, user.getSessionKey1());
            } else {
                response = client2.execute(request, user.getSessionKey2());
            }
            if (response.getBody().contains("item_update_delisting_response")) {
                good.setApproveStatus("instock");
                return true;
            } else {
                if (!response.getErrorCode().equals("7") && !response.getErrorCode().equals("530")) {
                    logService.log(user, response.getErrorCode() + " " + response.getMsg(), response.getSubMsg());
                } else {
                    logger.error(user.getNick() + ":" + flag + " " + response.getErrorCode() + " " + response.getMsg() + " " + response.getSubCode() + " " + response.getSubMsg());
                }
                return false;
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

}
