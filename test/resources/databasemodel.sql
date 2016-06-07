drop table if exists tbl_taxonomic_order;
drop table if exists tbl_taxonomic_order_systems;
drop table if exists tbl_taxa_tree_authors;
drop table if exists tbl_taxa_tree_master;
drop table if exists tbl_taxa_tree_genera;
drop table if exists tbl_taxa_tree_families;
drop table if exists tbl_taxa_tree_orders;
drop table if exists tbl_mcrdata_birmbeetledat;
drop table if exists tbl_biblio;
drop table if exists tbl_text_biology;
drop table if exists tbl_locations;
drop table if exists tbl_location_types;
drop table if exists tbl_taxonomic_notes;
drop table if exists tbl_text_identification_keys;
drop table if exists tbl_text_distribution;
drop table if exists tbl_mcr_summary_data;
drop table if EXISTS tbl_sites;

drop schema if exists bugs_import;

create schema bugs_import;
create table bugs_import.bugs_trace (
  bugs_trace_id integer generated by default as identity,
  bugs_table varchar(100) not null,
  bugs_data varchar,
  sead_table varchar(255) not null,
  sead_reference_id integer not null,
  import_date timestamp DEFAULT current_timestamp(),
  constraint pk_bugs_trace_bugs_trace_id primary key(bugs_trace_id)
);

create table bugs_import.bugs_errors (
  bugs_error_id integer generated by default as identity,
  bugs_table varchar(100) not null,
  bugs_data varchar,
  message longvarchar,
  import_date timestamp DEFAULT current_timestamp(),
  constraint pk_bugs_import_bugs_error_id primary key(bugs_error_id)
);

CREATE TABLE tbl_taxa_tree_orders
(
  order_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  order_name varchar(50) DEFAULT NULL,
  record_type_id integer,
  sort_order integer,
  constraint pk_taxa_tree_orders_order_id primary key(order_id)
);
CREATE TABLE tbl_taxa_tree_families
(
  family_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  family_name varchar(100) DEFAULT NULL,
  order_id integer NOT NULL,
  constraint pg_taxa_tree_families_family_id primary key(family_id),
  CONSTRAINT fk_taxa_tree_families_order_id FOREIGN KEY (order_id)
      REFERENCES tbl_taxa_tree_orders (order_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_taxa_tree_genera
(
  genus_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  family_id integer,
  genus_name varchar(100) DEFAULT NULL,
  constraint pk_taxa_tree_genera_genus_id primary key(genus_id),
  CONSTRAINT fk_taxa_tree_genera_family_id FOREIGN KEY (family_id)
      REFERENCES tbl_taxa_tree_families (family_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_taxa_tree_authors
(
  author_id integer generated by default as identity,
  author_name varchar(100) DEFAULT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  constraint pk_taxa_tree_authors_author_id primary key(author_id)
);
CREATE TABLE tbl_taxa_tree_master
(
  taxon_id integer generated by default as identity,
  author_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  genus_id integer,
  species varchar(255) DEFAULT NULL,
  constraint pk_taxa_tree_master_taxon_id primary key(taxon_id),
  CONSTRAINT fk_taxa_tree_master_author_id FOREIGN KEY (author_id)
      REFERENCES tbl_taxa_tree_authors (author_id)
      ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT fk_taxa_tree_master_genus_id FOREIGN KEY (genus_id)
      REFERENCES tbl_taxa_tree_genera (genus_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_taxonomic_order_systems
(
  taxonomic_order_system_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  system_description LONGVARCHAR,
  system_name varchar(50) DEFAULT NULL,
  constraint pk_taxonomic_order_systems_taxonomic_system_order_id primary key(taxonomic_order_system_id)
);
CREATE TABLE tbl_taxonomic_order
(
  taxonomic_order_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer DEFAULT 0,
  taxonomic_code decimal(18,10) DEFAULT 0,
  taxonomic_order_system_id integer DEFAULT 0,
  constraint pk_taxonomic_order_taxonomic_order_id primary key (taxonomic_order_id),
  CONSTRAINT fk_taxonomic_order_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id)
      ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_taxonomic_order_taxonomic_order_system_id FOREIGN KEY (taxonomic_order_system_id)
      REFERENCES tbl_taxonomic_order_systems (taxonomic_order_system_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbl_mcrdata_birmbeetledat
(
  mcrdata_birmbeetledat_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  mcr_data LONGVARCHAR,
  mcr_row smallint not null,
  taxon_id integer NOT NULL,
  CONSTRAINT pk_mcrdata_birmbeetledat PRIMARY KEY (mcrdata_birmbeetledat_id),
  CONSTRAINT fk_mcrdata_birmbeetledat_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id)
      ON UPDATE CASCADE ON DELETE NO ACTION
);
create table tbl_biblio
(
	biblio_id integer generated by default as identity,
	bugs_author varchar(255) DEFAULT NULL,
	bugs_reference varchar(60) DEFAULT NULL,
	bugs_title varchar,
	CONSTRAINT pk_biblio_id PRIMARY KEY (biblio_id)
);
create table tbl_text_biology
(
  biology_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  biology_text longvarchar,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  CONSTRAINT pk_text_biology PRIMARY KEY (biology_id),
  CONSTRAINT fk_text_biology_biblio_id FOREIGN KEY (biblio_id)
      REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_text_biology_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id) 
);
create table tbl_location_types
(
  location_type_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  description longvarchar,
  location_type varchar(40),
  CONSTRAINT pk_location_types PRIMARY KEY (location_type_id)
);
create table tbl_locations
(
  location_id integer generated by default as identity,
  location_name varchar(255) NOT NULL,
  location_type_id integer NOT NULL,
  default_lat_dd decimal(18,10),
  default_long_dd decimal(18,10),
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_locations PRIMARY KEY (location_id),
  CONSTRAINT fk_locations_location_type_id FOREIGN KEY (location_type_id)
      REFERENCES tbl_location_types (location_type_id)
);
create table tbl_taxonomic_notes (
  taxonomy_notes_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  taxonomy_notes longvarchar,
  CONSTRAINT pk_taxonomy_notes PRIMARY KEY (taxonomy_notes_id),
  CONSTRAINT fk_taxonomic_notes_biblio_id FOREIGN KEY (biblio_id)
      REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_taxonomic_notes_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id) 
);
create table tbl_text_identification_keys (
  key_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  key_text LONGVARCHAR,
  taxon_id integer NOT NULL,
  CONSTRAINT pk_text_identification_keys PRIMARY KEY (key_id),
  CONSTRAINT fk_text_identification_keys_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_text_identification_keys_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
create table tbl_text_distribution (
  distribution_id integer generated by default as identity,
  biblio_id integer NOT NULL,
  date_updated timestamp DEFAULT current_timestamp(),
  distribution_text LONGVARCHAR,
  taxon_id integer NOT NULL,
  CONSTRAINT pk_text_distribution PRIMARY KEY (distribution_id),
  CONSTRAINT fk_text_distribution_biblio_id FOREIGN KEY (biblio_id)
  REFERENCES tbl_biblio (biblio_id),
  CONSTRAINT fk_text_distribution_taxon_id FOREIGN KEY (taxon_id)
  REFERENCES tbl_taxa_tree_master (taxon_id)
);
create table tbl_mcr_summary_data(
  mcr_summary_data_id integer generated by default as identity,
  cog_mid_tmax smallint DEFAULT 0,
  cog_mid_trange smallint DEFAULT 0,
  date_updated timestamp DEFAULT current_timestamp(),
  taxon_id integer NOT NULL,
  tmax_hi smallint DEFAULT 0,
  tmax_lo smallint DEFAULT 0,
  tmin_hi smallint DEFAULT 0,
  tmin_lo smallint DEFAULT 0,
  trange_hi smallint DEFAULT 0,
  trange_lo smallint DEFAULT 0,
  CONSTRAINT pk_mcr_summary_data PRIMARY KEY (mcr_summary_data_id),
  CONSTRAINT fk_mcr_summary_data_taxon_id FOREIGN KEY (taxon_id)
      REFERENCES tbl_taxa_tree_master (taxon_id),
  CONSTRAINT key_mcr_summary_data_taxon_id UNIQUE (taxon_id)
);
create table tbl_sites (
  site_id integer generated by default as identity,
  altitude decimal(18,10),
  latitude_dd decimal(18,10),
  longitude_dd decimal(18,10),
  national_site_identifier varchar(255),
  site_description LONGVARCHAR DEFAULT NULL,
  site_name varchar(50) DEFAULT NULL,
  site_preservation_status_id integer,
  date_updated timestamp DEFAULT current_timestamp(),
  CONSTRAINT pk_sites PRIMARY KEY (site_id)
);
create table tbl_site_locations (
  site_location_id integer generated by default as identity,
  date_updated timestamp DEFAULT current_timestamp(),
  location_id integer NOT NULL,
  site_id integer NOT NULL,
  CONSTRAINT pk_site_location PRIMARY KEY (site_location_id),
  CONSTRAINT fk_locations_location_id FOREIGN KEY (location_id)
  REFERENCES tbl_locations (location_id),
  CONSTRAINT fk_locations_site_id FOREIGN KEY (site_id)
  REFERENCES tbl_sites (site_id)
);