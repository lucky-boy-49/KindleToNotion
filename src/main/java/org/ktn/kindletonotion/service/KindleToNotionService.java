package org.ktn.kindletonotion.service;

import org.ktn.kindletonotion.notion.model.Page;
import org.ktn.kindletonotion.utils.PageListToMapUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * kindle上传notion服务
 * @author 贺佳
 */
@Service
public class KindleToNotionService {

    public Map<String, Page> pagesToMap(List<Page> pages) {
        // 把数据库页下面的页转化为Map
        return PageListToMapUtil.toPgeMap(pages);
    }

}
