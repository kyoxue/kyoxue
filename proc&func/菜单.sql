-- 根据主菜单ID查找子菜单集合 函数
-- 先查找外键是null的 是一级菜单
-- 再根据一级的ID查找子菜单，如此往下。。。

DELIMITER $$
DROP FUNCTION IF EXISTS SP_FUNC_MENU$$
CREATE FUNCTION SP_FUNC_MENU(I_ID int(11)) returns MediumText
BEGIN
	DECLARE V_IDS MediumText;
	DECLARE O_IDS MediumText;
	IF (I_ID IS NULL) THEN
		RETURN '';
	END IF;
	SET group_concat_max_len = 2000000;
	SELECT id INTO V_IDS FROM r_menu WHERE id = I_ID and enable = 'Y';
	WHILE V_IDS IS NOT NULL DO
		IF (O_IDS IS NULL) THEN
			SET O_IDS = V_IDS;
		ELSE
			SET O_IDS = CONCAT(O_IDS,',',V_IDS);
		END IF;
		SELECT GROUP_CONCAT(id) INTO V_IDS FROM r_menu WHERE FIND_IN_SET(parentid,V_IDS);
	END WHILE;
	RETURN O_IDS;
END$$ 
DELIMITER ;

-- 查询SQL
SELECT
	T.ID,
	T.parentid,
	T.menu,
	T.url,
	T.sort
FROM
	r_menu T
WHERE
	FIND_IN_SET(
		T.id,
		SP_FUNC_MENU(1)
	)
and t.id !=1
order by t.sort asc

-- 测试函数
select SP_FUNC_MENU(2) from dual;

