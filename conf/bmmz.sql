select top_longitude,top_latitude from baomao_g65 where point_code like '%+000' OR point_code like '%+100' OR point_code like '%+200'  OR point_code like '%+300'  OR point_code like '%+400'
OR point_code like '%+500'  OR point_code like '%+600'  OR point_code like '%+700' OR point_code like '%+800' OR point_code like '%+900'
union ALL
select top_longitude,top_latitude from maozhan_g15 where point_code like '%+000' OR point_code like '%+100' OR point_code like '%+200'  OR point_code like '%+300'  OR point_code like '%+400'
OR point_code like '%+500'  OR point_code like '%+600'  OR point_code like '%+700' OR point_code like '%+800' OR point_code like '%+900'