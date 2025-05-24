import Link from "next/link"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { Send } from "lucide-react"

export default function Footer() {
  const currentYear = new Date().getFullYear()

  return (
    <footer className="bg-[#1C2449] text-white border-t border-zinc-800 py-12 mt-auto">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="text-xl font-bold mb-4">TicketNow</h3>
            <p className="text-zinc-400 mb-6 text-sm leading-relaxed">
              Nguồn vé đáng tin cậy của bạn cho các sự kiện trên toàn quốc. Mua vé nhanh chóng, an toàn và đảm bảo chính hãng cho mọi sự kiện yêu thích của bạn.
            </p>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Liên kết nhanh</h3>
            <ul className="space-y-2">
              {["Trang chủ", "Hòa nhạc", "Thể thao", "Kịch", "Lễ hội"].map((item) => (
                <li key={item}>
                  <Link href="#" className="text-zinc-400 hover:text-white transition-colors text-sm inline-block py-1">
                    {item}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Hỗ trợ khách hàng</h3>
            <ul className="space-y-2">
              {["Liên hệ", "Câu hỏi thường gặp", "Chính sách hoàn tiền", "Điều khoản dịch vụ", "Chính sách bảo mật"].map((item) => (
                <li key={item}>
                  <Link href="#" className="text-zinc-400 hover:text-white transition-colors text-sm inline-block py-1">
                    {item}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Cập nhật mới nhất</h3>
            <p className="text-zinc-400 mb-4 text-sm">Đăng ký nhận bản tin để nhận ưu đãi và cập nhật mới nhất.</p>
            <div className="flex w-full max-w-sm items-center space-x-2">
              <div className="relative flex-1">
                <Input
                  type="email"
                  placeholder="Email của bạn"
                  className="bg-zinc-900 border-zinc-800 text-white placeholder:text-zinc-500 focus-visible:ring-zinc-700 pr-10"
                />
                <Button
                  type="submit"
                  size="icon"
                  className="absolute right-1 top-1/2 -translate-y-1/2 h-7 w-7 bg-zinc-800 hover:bg-zinc-700"
                >
                  <Send className="h-3.5 w-3.5" />
                </Button>
              </div>
            </div>
            <p className="text-zinc-500 text-xs mt-2">Chúng tôi tôn trọng quyền riêng tư của bạn. Bạn có thể hủy đăng ký bất kỳ lúc nào.</p>
          </div>
        </div>

        <Separator className="my-8 bg-zinc-800" />

        <div className="flex flex-col md:flex-row justify-between items-center">
          <p className="text-zinc-500 text-sm mb-4 md:mb-0">© {currentYear} TicketNow. Mọi quyền được bảo lưu.</p>
          <div className="flex space-x-6">
            <Link href="#" className="text-zinc-500 hover:text-white text-xs">
              Điều khoản
            </Link>
            <Link href="#" className="text-zinc-500 hover:text-white text-xs">
              Bảo mật
            </Link>
            <Link href="#" className="text-zinc-500 hover:text-white text-xs">
              Cookie
            </Link>
          </div>
        </div>
      </div>
    </footer>
  )
}
