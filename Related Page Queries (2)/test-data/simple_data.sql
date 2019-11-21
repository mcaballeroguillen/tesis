DROP TABLE IF EXISTS links;
CREATE TABLE links (
  link_id int(11) NOT NULL auto_increment,
  id_page_from int(11) NOT NULL default '0',
  id_page_to int(11) NOT NULL default '0',
  PRIMARY KEY  (link_id),
  UNIQUE KEY link_id (link_id),
  KEY link_id_2 (link_id)
) TYPE=MyISAM;

#
# Dumping data for table `links`
#

INSERT INTO links (link_id, id_page_from, id_page_to) VALUES (1,1,2);
INSERT INTO links (link_id, id_page_from, id_page_to) VALUES (2,1,3);
INSERT INTO links (link_id, id_page_from, id_page_to) VALUES (3,2,3);
INSERT INTO links (link_id, id_page_from, id_page_to) VALUES (4,3,1);
INSERT INTO links (link_id, id_page_from, id_page_to) VALUES (5,4,3);
# --------------------------------------------------------

#
# Table structure for table `pages`
#

DROP TABLE IF EXISTS pages;
CREATE TABLE pages (
  page_id int(11) NOT NULL auto_increment,
  name varchar(50) NOT NULL default '',
  pr double NOT NULL default '1',
  temp_calc_temp_pr double NOT NULL default '0',
  temp_calc_nr_of_links_out int(11) NOT NULL default '0',
  temp_calc_new_pr double NOT NULL default '0',
  PRIMARY KEY  (page_id),
  UNIQUE KEY page_id (page_id),
  KEY page_id_2 (page_id)
) TYPE=MyISAM;

#
# Dumping data for table `pages`
#

INSERT INTO pages (page_id, name, pr, temp_calc_temp_pr, temp_calc_nr_of_links_out, temp_calc_new_pr) VALUES (1,'A','9.582875','1.91515625',2,'');
INSERT INTO pages (page_id, name, pr, temp_calc_temp_pr, temp_calc_nr_of_links_out, temp_calc_new_pr) VALUES (2,'B','4.12640625','1.8795625',1,'');
INSERT INTO pages (page_id, name, pr, temp_calc_temp_pr, temp_calc_nr_of_links_out, temp_calc_new_pr) VALUES (3,'C','10.61721875','5.076625',1,'');
INSERT INTO pages (page_id, name, pr, temp_calc_temp_pr, temp_calc_nr_of_links_out, temp_calc_new_pr) VALUES (4,'D','1','0.85',1,'');

    


