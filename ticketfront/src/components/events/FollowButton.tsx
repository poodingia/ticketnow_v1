'use client'

import {useFollowEvent} from "@/hooks/useFetch";
import {updateFollowEvent} from "@/api/account";
import {Heart} from "lucide-react";
import {Button} from "@/components/ui/button";

export default function FollowButton({id}: { id: string }) {
  const {data: isFollowing, refetch} = useFollowEvent(id);
  const handleFollow = async () => {
    if (isFollowing !== undefined) {
      await updateFollowEvent(id, isFollowing);
      refetch().then()
    }
  }
  return (
    <Button onClick={() => handleFollow()} className="w-full sm:w-auto">
      <Heart className={`w-4 h-4 mr-2 transition-all ${isFollowing ? "fill-current" : "fill-none"}`}/>
      {isFollowing ? "Bỏ theo dõi" : "Theo dõi"}
    </Button>
  )

}