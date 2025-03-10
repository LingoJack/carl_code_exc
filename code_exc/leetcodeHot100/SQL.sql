-- 上级经理已离职的公司员工 
select
    employee_id
from
    Employees e1
where
    salary < 30000
    and manager_id is not NULL
    and not exists (
        select
            e2.employee_id
        from
            Employees e2
        where
            e2.employee_id = e1.manager_id
    )
order by
    employee_id;

-- 可回收且低脂的产品
select
    product_id
from
    Products
where
    low_fats = 'Y'
    and recyclable = 'Y';


-- 寻找用户推荐人
select name
from Customer
where referee_id != 2 or referee_id is NULL;