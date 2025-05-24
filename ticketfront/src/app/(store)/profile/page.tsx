'use client'

import ProfileForm from "@/components/profile/ProfilePage";
import {useProfile} from "@/hooks/useFetch";
import Loading from "@/app/loading";

export default function Home(){
  const {data: customer, isPending, error,} = useProfile();

  if (isPending) {
    return <Loading />
  }

  if (error) {
    return <div className="flex justify-center items-center h-screen">An error occurred: {error.message}</div>
  }

  return (
    <div className="container mx-auto py-10">
      <ProfileForm customer={customer} />
    </div>
  )
}