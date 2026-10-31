package com.example.adidusshop.services

import com.example.adidusshop.models.CartItem
import com.example.adidusshop.models.OrderGroup
import com.example.adidusshop.models.OrderItem
import com.example.adidusshop.models.Product
import com.example.adidusshop.models.ProductVariant

object MockData {
    val sampleProducts = listOf(
        Product(
            product_id = 1,
            name = "Adidas Samba OG",
            description = "Sneaker",
            price = 3800,
            image_filename = "lifestyle1",
            brand = "Adidas Originals",
            stock_quantity = 25,
            category_name = "lifestyle",
            detail = "รองเท้าผ้าใบระดับตำนานจากอาดิดาส อัปเปอร์หนังแท้สัมผัสนุ่ม เสริมแถบ 3-Stripes ที่เป็นไอคอนิก เหมาะสำหรับทุกการแต่งกายในชีวิตประจำวัน",
            variants = listOf(
                ProductVariant(101, 1, 5, 3800, "7.0 UK", "Cloud White / Core Black"),
                ProductVariant(102, 1, 8, 3800, "8.0 UK", "Cloud White / Core Black"),
                ProductVariant(103, 1, 10, 3800, "8.5 UK", "Cloud White / Core Black"),
                ProductVariant(104, 1, 6, 3800, "9.0 UK", "Cloud White / Core Black"),
                ProductVariant(105, 1, 4, 3800, "9.5 UK", "Cloud White / Core Black"),
                ProductVariant(106, 1, 2, 3800, "10.0 UK", "Cloud White / Core Black")
            )
        ),
        Product(
            product_id = 2,
            name = "Adidas Ultraboost Light",
            description = "Sneaker",
            price = 6500,
            image_filename = "lifestyle2",
            brand = "Adidas Performance",
            stock_quantity = 18,
            category_name = "lifestyle",
            detail = "สัมผัสพลังที่ไม่สิ้นสุดกับเทคโนโลยี Light BOOST ที่เบาที่สุดเท่าที่อาดิดาสเคยพัฒนามา ออกแบบเพื่อการวิ่งและความสบายระดับสูงสุดตลอดวัน",
            variants = listOf(
                ProductVariant(201, 2, 4, 6500, "8.0 UK", "Core Black / Cloud White"),
                ProductVariant(202, 2, 6, 6500, "8.5 UK", "Core Black / Cloud White"),
                ProductVariant(203, 2, 8, 6500, "9.0 UK", "Core Black / Cloud White"),
                ProductVariant(204, 2, 5, 6500, "10.0 UK", "Core Black / Cloud White")
            )
        ),
        Product(
            product_id = 3,
            name = "Adidas Predator Elite FG",
            description = "Football",
            price = 8500,
            image_filename = "football1",
            brand = "Adidas Football",
            stock_quantity = 12,
            category_name = "football",
            detail = "รองเท้าฟุตบอลสำหรับผู้เล่นสายคุมเกม อัปเปอร์ HybridTouch 2.0 พร้อมครีบยาง Strikeskin ให้การยิงประตูและสัมผัสบอลแม่นยำเฉียบคม",
            variants = listOf(
                ProductVariant(301, 3, 3, 8500, "7.5 UK", "Solar Red / Core Black"),
                ProductVariant(302, 3, 5, 8500, "8.5 UK", "Solar Red / Core Black"),
                ProductVariant(303, 3, 4, 8500, "9.5 UK", "Solar Red / Core Black")
            )
        ),
        Product(
            product_id = 4,
            name = "Adidas Copa Pure II Pro",
            description = "Football",
            price = 4800,
            image_filename = "football2",
            brand = "Adidas Football",
            stock_quantity = 15,
            category_name = "football",
            detail = "รองเท้าสตั๊ดหนัง Fusionskin ให้ความคลาสสิก นุ่มสบายเท้า และมอบสัมผัสที่ละเมียดละไมสำหรับคอบอลสไตล์คลาสสิก",
            variants = listOf(
                ProductVariant(401, 4, 4, 4800, "8.0 UK", "Ivory / Core Black"),
                ProductVariant(402, 4, 6, 4800, "9.0 UK", "Ivory / Core Black"),
                ProductVariant(403, 4, 5, 4800, "10.0 UK", "Ivory / Core Black")
            )
        ),
        Product(
            product_id = 5,
            name = "Adidas Adilette 22 Slides",
            description = "FlipFlops",
            price = 2200,
            image_filename = "slipper1",
            brand = "Adidas Originals",
            stock_quantity = 30,
            category_name = "flipflops",
            detail = "รองเท้าแตะทรงล้ำยุคที่ได้รับแรงบันดาลใจจากแผนที่ภูมิประเทศของดาวอังคาร ผลิตจากวัสดุชีวภาพ EVA สวมใส่สบายและเป็นมิตรกับสิ่งแวดล้อม",
            variants = listOf(
                ProductVariant(501, 5, 10, 2200, "7.0 UK", "Desert Sand"),
                ProductVariant(502, 5, 12, 2200, "8.0 UK", "Desert Sand"),
                ProductVariant(503, 5, 8, 2200, "9.0 UK", "Desert Sand")
            )
        ),
        Product(
            product_id = 6,
            name = "Adidas Adilette Comfort",
            description = "FlipFlops",
            price = 1500,
            image_filename = "slipper2",
            brand = "Adidas Core",
            stock_quantity = 40,
            category_name = "flipflops",
            detail = "รองเท้าแตะพื้น Cloudfoam Plus นุ่มกระชับเท้า ฟื้นฟูความเมื่อยล้าหลังออกกำลังกาย ลาย 3-Stripes คอนทราสต์สะดุดตา",
            variants = listOf(
                ProductVariant(601, 6, 15, 1500, "8.0 UK", "Core Black / White"),
                ProductVariant(602, 6, 15, 1500, "9.0 UK", "Core Black / White"),
                ProductVariant(603, 6, 10, 1500, "10.0 UK", "Core Black / White")
            )
        )
    )

    val mockCartItems = mutableListOf(
        CartItem(
            cart_id = 1,
            quantity = 1,
            price = 3800,
            product_id = 1,
            name = "Adidas Samba OG",
            description = "Sneaker",
            image_filename = "lifestyle1",
            size = "8.5 UK",
            stock_quantity = 10
        ),
        CartItem(
            cart_id = 2,
            quantity = 1,
            price = 2200,
            product_id = 5,
            name = "Adidas Adilette 22 Slides",
            description = "FlipFlops",
            image_filename = "slipper1",
            size = "8.0 UK",
            stock_quantity = 12
        )
    )

    val mockOrders = mutableListOf(
        OrderGroup(
            order_id = 1001,
            total_price = 7600.0,
            payment_method = "โอนผ่าน PromptPay",
            address = "99/1 ซอยอารีย์ ถนนพหลโยธิน แขวงพญาไท เขตพญาไท กรุงเทพฯ 10400",
            items = listOf(
                OrderItem(
                    cart_id = 91,
                    quantity = 2,
                    price = 3800,
                    product_id = 1,
                    name = "Adidas Samba OG",
                    description = "Sneaker",
                    image_filename = "lifestyle1",
                    size = "9.0 UK",
                    order_id = 1001,
                    total_price = 7600.0,
                    payment_method = "โอนผ่าน PromptPay",
                    house_number = "99/1",
                    village_number = "5",
                    subdistrict = "พญาไท",
                    district = "พญาไท",
                    province = "กรุงเทพมหานคร",
                    postal_code = "10400"
                )
            )
        )
    )
}
