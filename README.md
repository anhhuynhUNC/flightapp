# flightapp
Web có 3 phần em để là Hiển thị bảng giá, hiển thị theo tháng - ngày và hiển thị theo list.
# Bảng giá
Bảng giá fetch data qua 2 api, hiển thị tất cả các chuyến bay rẻ nhất (và đắt nhất) trong một ngày. Chuyến nào rẻ nhất trong tháng thì có hightlight.
Số tháng được tính là 6 tháng tính từ tháng hiện tại.
Nếu qua một tháng thì phải update db tạo thêm các ngày tháng sau. (Cái này trong backend em chưa làm, nhưng chỉ đơn giản là sau một thời gian cứ insert vào db).
Phần đắt nhất thì em để default off.
# Hiển thị theo tháng, ngày
Fetch qua 2 api của tháng/ngày
Người dùng click vào bảng tháng hoặc ngày thì sẽ hiện bảng của tháng/ngày đó.
Có thể tắt bảng đi.
Có thể sort theo ngày/ giá.
Hiện tại api lấy theo tháng em để delay 5s để giả vờ xem request bị chậm. Nếu người dùng request theo tháng hoặc ngày thì app ngăn tương tác, để người ta không spam.
# Hiển thị theo List
Qua 2 api.
Chỉ tạo một list tất cả các chuyến bay của chặng và loại vé đấy, tính từ ngày truy cập.
Sort được theo ngày/ giá.
