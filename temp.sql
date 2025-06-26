CREATE TABLE users (
    `user_id` UInt64,
    `gender` Enum8('male'=1, 'female'=2, 'unknown'=0),
    `cell_id` UInt32,
    `region_id` UInt32,
    `age` UInt8,
    `update_time` DateTime DEFAULT now(),
    
    INDEX idx_region_gender region_id TYPE bloom_filter GRANULARITY 4,
    INDEX idx_age age TYPE minmax GRANULARITY 2,
    INDEX idx_cell cell_id TYPE bloom_filter GRANULARITY 4
) ENGINE = ReplicatedReplacingMergeTree('/clickhouse/tables/{shard}/users', '{replica}', update_time)
ORDER BY (region_id, cell_id, user_id)
PARTITION BY (region_id, toYYYYMM(update_time))
TTL update_time + INTERVAL 30 DAY
SETTINGS index_granularity = 8192;

CREATE TABLE cell_user_bitmaps (
    `timestamp` DateTime,
    `region_id` UInt64,
    `total_bitmap` AggregateFunction(groupBitmap, UInt64),
    `male_bitmap` AggregateFunction(groupBitmap, UInt64),
    `female_bitmap` AggregateFunction(groupBitmap, UInt64),
    `age_10_20_bitmap` AggregateFunction(groupBitmap, UInt64),
    `age_20_40_bitmap` AggregateFunction(groupBitmap, UInt64),
    `age_40_plus_bitmap` AggregateFunction(groupBitmap, UInt64),
  
    INDEX idx_timestamp timestamp TYPE minmax GRANULARITY 3,
    INDEX idx_region region_id TYPE bloom_filter GRANULARITY 3
) ENGINE = ReplicatedReplacingMergeTree('/clickhouse/tables/{shard}/region_bitmaps', '{replica}')
PARTITION BY toYYYYMMDD(timestamp)
ORDER BY (timestamp, region_id)
TTL timestamp + INTERVAL 30 DAY
SETTINGS index_granularity = 8192;