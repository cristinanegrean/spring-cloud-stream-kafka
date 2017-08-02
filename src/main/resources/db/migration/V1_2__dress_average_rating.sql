update dress set average_rating = (
  select coalesce(round(avg(stars)), 0) from rating where dress.id=rating.dress_id);