--Oracle

create table baby (
    id number(10) not null, 
    name varchar2(30) not null, 
    birthday date not null, 
constraint baby_pk primary key (id));

declare
    type array_t is varray(10) of varchar2(10);
    v_names array_t := array_t('Letizia', 'Giselle', 'Leandro', 'Juan', 'Claudio', 'Jorge', 'Carla', 'Michelle', 'Ludmila', 'Mauro');
    v_dates array_t := array_t('01/01', '02/02', '03/03', '04/04', '05/05', '06/06', '07/07', '08/08', '09/09', '10/10');
    v_loop number;
    v_pos number;
begin
	for v_loop in 1 .. 100000
    loop
        select floor(dbms_random.value(1, 10)) into v_pos from dual;
    
        insert into baby (id, name, birthday)
        values (v_loop, v_names(v_pos), to_date(v_dates(v_pos) || '/2000', 'dd/mm/yyyy'));
    end loop;
end;

--MySQL

create table baby (
    id int not null, 
    name varchar(30) not null, 
    birthday date not null, 
    primary key (id));

begin
    declare v_names varchar(100); 
    declare v_dates varchar(100); 
    declare v_name varchar(10);
    declare v_date varchar(10);
    declare v_loop int;
    declare v_pos int;
    
    declare v_pos_aux int;
    declare v_ref int;
    
    start transaction;
    
    set v_names = 'Letizia,Giselle,Leandro,Juan,Claudio,Jorge,Carla,Michelle,Ludmila,Mauro,';
    set v_dates = '01/01,02/02,03/03,04/04,05/05,06/06,07/07,08/08,09/09,10/10,';
    
    set v_loop = 0;
    while v_loop < 100000
    do
        set v_pos = (select floor((rand() * (10-1+1))+1) from dual);
    
		set v_ref = 0; 
		set v_pos_aux = 1;
        while (v_pos_aux < v_pos)
		do
		    set v_ref = locate(',', v_names, v_ref + 1);
		    
		    set v_pos_aux = v_pos_aux + 1; 
        end while;
	    set v_name = substring(v_names, v_ref + 1, locate(',', v_names, v_ref + 1) - 1 - v_ref);
        
        set v_ref = 0;
        set v_pos_aux = 1;
        while (v_pos_aux < v_pos)
        do
            set v_ref = locate(',', v_dates, v_ref + 1);
            
            set v_pos_aux = v_pos_aux + 1; 
        end while;
        set v_date = substring(v_dates, v_ref + 1, locate(',', v_dates, v_ref + 1) - 1 - v_ref);
        
        insert into baby (id, name, birthday)
        values (v_loop + 1, v_name, str_to_date(concat(v_date, '/2000'), '%d/%m/%Y'));
        
        set v_loop = v_loop + 1;
    end while;
    
    commit;
end;