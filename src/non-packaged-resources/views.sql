create view staging_channel as
select distinct
    channel_id,
    channel_title
from playlist;

create view video_staging as
select
    video_id,
    (((contents->>'snippet')::jsonb)->>'publishedAt')::timestamp as published,
    (((contents->>'contentDetails')::jsonb)->>'duration')::interval as duration,
    (((contents->>'contentDetails')::jsonb)->>'caption')::boolean as caption,
    ((contents->>'contentDetails')::jsonb)->>'definition' as definition,
    ((contents->>'snippet')::jsonb)->>'title' as title,
    ((contents->>'snippet')::jsonb)->>'description' as description,
    (((contents->>'statistics')::jsonb)->>'viewCount')::int8 as view_count,
    (((contents->>'statistics')::jsonb)->>'likeCount')::int as like_count,
    (((contents->>'statistics')::jsonb)->>'dislikeCount')::int as dislike_count,
    (((contents->>'statistics')::jsonb)->>'favoriteCount')::int as favorite_count,
    (((contents->>'statistics')::jsonb)->>'commentCount')::int as comment_count,
    contents->>'player' as player
from raw_video;

create view tag as
select
    video_id,
    t.seqnum,
    t.tag
from
    raw_video
cross join lateral
    jsonb_array_elements_text((((contents->>'snippet')::jsonb)->>'tags')::jsonb) with ordinality as t(tag,seqnum)
;

create view video_thumbnail_staging as
select
    video_id,
    'default' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'default')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'default')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'default')::jsonb)->>'height')::int as height
from raw_video
union
select
    video_id,
    'medium' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'medium')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'medium')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'medium')::jsonb)->>'height')::int as height
from raw_video
union
select
    video_id,
    'high' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'high')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'high')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'high')::jsonb)->>'height')::int as height
from raw_video
union
select
    video_id,
    'standard' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'standard')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'standard')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'standard')::jsonb)->>'height')::int as height
from raw_video
union
select
    video_id,
    'maxres' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'maxres')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'maxres')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'maxres')::jsonb)->>'height')::int as height
from raw_video
;

create view playlist_item as
select
    playlist_id,
    ((((contents->>'snippet')::jsonb)->>'resourceId')::jsonb)->>'videoId' as video_id
from raw_playlist_item;

create view playlist_staging as
select
    channel_id,
    contents->>'id' as playlist_id,
    contents->>'etag' as etag,
    (((contents->>'snippet')::jsonb)->>'publishedAt')::timestamp as published,
    ((contents->>'snippet')::jsonb)->>'title' as title,
    ((contents->>'snippet')::jsonb)->>'description' as description
from raw_playlist;

create view search_result as
select
    video_id,
    ((contents->>'snippet')::jsonb)->>'title' as title,
    ((contents->>'snippet')::jsonb)->>'description' as description,
    (((contents->>'snippet')::jsonb)->>'publishedAt')::timestamp as published,
    ((contents->>'snippet')::jsonb)->>'channelId' as channel_id,
    ((contents->>'snippet')::jsonb)->>'channelTitle' as channel_title,
    contents->>'etag' as etag,
    ((contents->>'id')::jsonb)->>'kind' as kind,
    year
from raw;

create view search_thumbnail as
select
    video_id,
    'default' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'default')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'default')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'default')::jsonb)->>'height')::int as height,
    year
from raw
union
select
    video_id,
    'high' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'high')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'high')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'high')::jsonb)->>'height')::int as height,
    year
from raw
union
select
    video_id,
    'medium' as size,
    ((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'medium')::jsonb)->>'url' as url,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'medium')::jsonb)->>'width')::int as width,
    (((((((contents->>'snippet')::jsonb)->>'thumbnails')::jsonb)->>'medium')::jsonb)->>'height')::int as height,
    year
from raw
;
