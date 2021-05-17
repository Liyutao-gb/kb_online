package com.atguigu.vod.service.impl;

import com.atguigu.vod.dao.MembersDao;
import com.atguigu.vod.entity.Members;
import com.atguigu.vod.service.MembersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Members)表服务实现类
 *
 * @author makejava
 * @since 2021-05-17 13:22:39
 */
@Service("membersService")
public class MembersServiceImpl implements MembersService {
    @Resource
    private MembersDao membersDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Members queryById(Integer id) {
        return this.membersDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Members> queryAllByLimit(int offset, int limit) {
        return this.membersDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param members 实例对象
     * @return 实例对象
     */
    @Override
    public Members insert(Members members) {
        this.membersDao.insert(members);
        return members;
    }

    /**
     * 修改数据
     *
     * @param members 实例对象
     * @return 实例对象
     */
    @Override
    public Members update(Members members) {
        this.membersDao.update(members);
        return this.queryById(members.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.membersDao.deleteById(id) > 0;
    }
}
